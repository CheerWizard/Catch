package com.cws.printer

import io.ktor.client.HttpClient
import io.ktor.client.engine.darwin.Darwin
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json

actual fun provideHttpClient(): HttpClient = HttpClient(Darwin) {
    install(ContentNegotiation) {
        json()
    }
}