package com.cws.printer

import io.ktor.client.HttpClient
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.cancel
import kotlinx.coroutines.delay
import kotlinx.coroutines.isActive
import kotlinx.coroutines.launch
import kotlinx.serialization.json.addJsonObject
import kotlinx.serialization.json.buildJsonObject
import kotlinx.serialization.json.put
import kotlinx.serialization.json.putJsonArray
import kotlinx.serialization.json.putJsonObject
import kotlin.time.Duration.Companion.seconds

class FirebaseLogger(
    val clientId: String,
    val apiSecret: String,
    val measurementId: String
) {

    companion object {
        private const val TAG = "FirebaseLogger"

        private val SEND_PERIOD = 10.seconds

        private const val BASE_URL = "https://www.google-analytics.com/mp/collect"
        private const val CLIENT_ID = "client_id"
        private const val MEASUREMENT_ID = "measurement_id"
        private const val API_SECRET = "api_secret"
    }

    private var httpClient: HttpClient? = null
    private var scope: CoroutineScope? = null
    private val logs = StringBuilder()
    private val consoleLogger = ConsoleLogger()

    fun open() {
        if (scope?.isActive == true) return
        httpClient = provideHttpClient()
        scope = CoroutineScope(Dispatchers.Default)
        scope?.launch {
            while (isActive) {
                delay(SEND_PERIOD)
                sendLogs()
            }
        }
    }

    fun close() {
        scope?.cancel()
        scope = null
        httpClient?.let { client ->
            if (client.isActive) {
                client.close()
            }
        }
        httpClient = null
    }

    fun log(logLevel: LogLevel, tag: String, message: String, exception: Throwable? = null) {
        logs.appendLine(formatLog(logLevel, tag, message, exception))
    }

    private suspend fun sendLogs() {
        val httpClient = this.httpClient
        if (httpClient == null) return

        val log = logs.toString()
        logs.clear()

        val requestBody = buildJsonObject {
            put(CLIENT_ID, clientId)
            putJsonArray("events") {
                addJsonObject {
                    put("name", "log")
                    putJsonObject("params") {
                        put("message", log)
                    }
                }
            }
        }

        val response = httpClient.post(BASE_URL) {
            url {
                parameters.append(MEASUREMENT_ID, measurementId)
                parameters.append(API_SECRET, apiSecret)
            }
            contentType(ContentType.Application.Json)
            setBody(requestBody.toString())
        }

        if (response.status == HttpStatusCode.OK) {
            consoleLogger.log(LogLevel.INFO, TAG, "Log successfully sent")
        } else {
            consoleLogger.log(LogLevel.ERROR, TAG, "Failed to send log")
        }
    }

}