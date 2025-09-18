package com.cws.acatch.game.platform

import com.cws.kanvas.input.InputSensorManager
import org.koin.core.module.Module
import org.koin.dsl.module

actual val platformModule: Module = module {
    single<InputSensorManager> { InputSensorManager(get()) }
    single<Preferences> { PreferencesImpl(get(), "catch_prefs") }
}