package com.cws.acatch.game.di

import com.cws.acatch.game.GameLoop
import com.cws.acatch.game.networking.user.UserPreferences
import org.koin.dsl.module

val commonModule = module {
    single<GameLoop> {
        GameLoop(
            x = 0,
            y = 0,
            width = 800,
            height = 600,
            title = "",
            inputSensorManager = get()
        )
    }
    factory<UserPreferences> { UserPreferences(get()) }
}