package com.cws.acatch.storage

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class PreferencesImpl(context: Context, name: String) : Preferences {

    private val _preferences: SharedPreferences =
        context.getSharedPreferences(name, Context.MODE_PRIVATE)

    override fun setByte(key: String, value: Byte) {
        _preferences.edit(commit = true) {
            putInt(key, value.toInt())
        }
    }

    override fun setBoolean(key: String, value: Boolean) {
        _preferences.edit(commit = true) {
            putBoolean(key, value)
        }
    }

    override fun setShort(key: String, value: Short) {
        _preferences.edit(commit = true) {
            putInt(key, value.toInt())
        }
    }

    override fun setInt(key: String, value: Int) {
        _preferences.edit(commit = true) {
            putInt(key, value)
        }
    }

    override fun setLong(key: String, value: Long) {
        _preferences.edit(commit = true) {
            putLong(key, value)
        }
    }

    override fun setFloat(key: String, value: Float) {
        _preferences.edit(commit = true) {
            putFloat(key, value)
        }
    }

    override fun setDouble(key: String, value: Double) {
        _preferences.edit(commit = true) {
            putString(key, value.toString())
        }
    }

    override fun setString(key: String, value: String) {
        _preferences.edit(commit = true) {
            putString(key, value)
        }
    }

    override fun getByte(key: String, default: Byte): Byte {
        return _preferences.getInt(key, default.toInt()).toByte()
    }

    override fun getBoolean(key: String, default: Boolean): Boolean {
        return _preferences.getBoolean(key, default)
    }

    override fun getShort(key: String, default: Short): Short {
        return _preferences.getInt(key, default.toInt()).toShort()
    }

    override fun getInt(key: String, default: Int): Int {
        return _preferences.getInt(key, default)
    }

    override fun getLong(key: String, default: Long): Long {
        return _preferences.getLong(key, default)
    }

    override fun getFloat(key: String, default: Float): Float {
        return _preferences.getFloat(key, default)
    }

    override fun getDouble(key: String, default: Double): Double {
        return _preferences.getString(key, default.toString())?.toDoubleOrNull() ?: default
    }

    override fun getString(key: String, default: String): String {
        return _preferences.getString(key, default) ?: default
    }

    override fun remove(key: String) {
        _preferences.edit(commit = true) {
            remove(key)
        }
    }

    override fun commit() {
        _preferences.edit(commit = true) {}
    }

    override fun sync() {
        // do nothing for now
    }

}