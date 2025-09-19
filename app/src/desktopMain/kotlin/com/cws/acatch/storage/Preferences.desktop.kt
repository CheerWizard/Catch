package com.cws.acatch.storage

class PreferencesImpl(name: String) : Preferences {

    private val _preferences = java.util.prefs.Preferences.userRoot().node(name)

    override fun setByte(key: String, value: Byte) {
        _preferences.putInt(key, value.toInt())
    }

    override fun setBoolean(key: String, value: Boolean) {
        _preferences.putBoolean(key, value)
    }

    override fun setShort(key: String, value: Short) {
        _preferences.putInt(key, value.toInt())
    }

    override fun setInt(key: String, value: Int) {
        _preferences.putInt(key, value)
    }

    override fun setLong(key: String, value: Long) {
        _preferences.putLong(key, value)
    }

    override fun setFloat(key: String, value: Float) {
        _preferences.putFloat(key, value)
    }

    override fun setDouble(key: String, value: Double) {
        _preferences.putDouble(key, value)
    }

    override fun setString(key: String, value: String) {
        _preferences.put(key, value)
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
        return _preferences.getDouble(key, default)
    }

    override fun getString(key: String, default: String): String {
        return _preferences.get(key, default)
    }

    override fun remove(key: String) {
        _preferences.remove(key)
    }

    override fun commit() {
        _preferences.flush()
    }

    override fun sync() {
        _preferences.sync()
    }

}