package com.cws.kanvas

import android.content.Context
import android.content.res.AssetManager

object AssetProvider {

    lateinit var assets: AssetManager
        private set

    fun init(context: Context) {
        assets = context.assets
    }

}