package com.cws.fmm

import com.cws.printer.Printer
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.DelicateCoroutinesApi
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.cancel
import kotlinx.coroutines.isActive
import kotlin.time.Clock
import kotlin.time.Duration
import kotlin.time.Duration.Companion.nanoseconds
import kotlin.time.ExperimentalTime

object FastProfiler {

    private const val TAG = "FastProfiler"
    private var scope: CoroutineScope? = null

    @OptIn(DelicateCoroutinesApi::class)
    fun start() {
        if (scope?.isActive == true) return
        scope = CoroutineScope(Dispatchers.Default + SupervisorJob())
    }

    fun stop() {
        scope?.cancel()
        scope = null
    }

    fun profile(name: String, actualDuration: Duration, expectedDuration: Duration) {
        if (actualDuration > expectedDuration && expectedDuration != 0.nanoseconds) {
            Printer.w(TAG, "$name() - spent ${actualDuration.inWholeMilliseconds} ms, expected ${expectedDuration.inWholeMilliseconds} ms")
        } else {
            Printer.d(TAG, "$name() - spent ${actualDuration.inWholeMilliseconds} ms")
        }
    }

}

@OptIn(ExperimentalTime::class)
inline fun <reified T> profile(name: String, expectedDuration: Duration = 0.nanoseconds, function: () -> T) {
    val startMillis = Clock.System.now().toEpochMilliseconds().nanoseconds
    function()
    val endMillis = Clock.System.now().toEpochMilliseconds().nanoseconds
    val actualDuration = endMillis - startMillis
    FastProfiler.profile(name, actualDuration = actualDuration, expectedDuration = expectedDuration)
}