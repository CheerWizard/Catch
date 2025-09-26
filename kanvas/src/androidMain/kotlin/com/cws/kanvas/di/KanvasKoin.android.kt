package com.cws.kanvas.di

import android.content.Context
import com.cws.printer.Printer
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.koin.androidLogger
import org.koin.core.KoinApplication
import org.koin.core.logger.Level
import org.koin.core.module.KoinApplicationDslMarker
import org.koin.dsl.KoinAppDeclaration

@KoinApplicationDslMarker
fun startKanvasKoin(
    context: Context,
    before: KoinAppDeclaration = {},
    after: KoinAppDeclaration = {}
): KoinApplication = startKanvasKoin(before = {
    androidContext(context)
    androidLogger(if (Printer.enabled) Level.DEBUG else Level.NONE)
    before()
}, after = after)