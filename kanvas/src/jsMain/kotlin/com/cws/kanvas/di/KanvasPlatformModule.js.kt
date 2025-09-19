package com.cws.kanvas.di

import com.cws.kanvas.ShaderLoader
import com.cws.kanvas.ShaderLoaderImpl
import org.koin.core.module.Module
import org.koin.dsl.module

actual val kanvasPlatformModule: Module = module {
    factory<ShaderLoader> { ShaderLoaderImpl() }
}