package com.cws.acatch.networking.user

import com.cws.acatch.storage.Preferences

class UserPreferences(
    private val preferences: Preferences
) {

    private object Keys {
        const val SESSION = "SESSION"
    }

    fun setSession(session: String) = preferences.setString(Keys.SESSION, session)

    fun getSession(): String = preferences.getString(Keys.SESSION, "")

    fun clearSession() = preferences.remove(Keys.SESSION)

}