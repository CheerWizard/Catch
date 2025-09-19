package com.cws.kanvas.di

import org.koin.core.KoinApplication
import org.koin.core.context.startKoin
import org.koin.core.module.KoinApplicationDslMarker
import org.koin.dsl.KoinAppDeclaration

@KoinApplicationDslMarker
fun startKanvasKoin(
    before: KoinAppDeclaration = {},
    after: KoinAppDeclaration = {}
): KoinApplication = startKoin {
    before()
    modules(kanvasModule, kanvasPlatformModule)
    after()
}