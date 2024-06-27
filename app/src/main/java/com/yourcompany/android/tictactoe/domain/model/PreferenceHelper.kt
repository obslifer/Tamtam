package com.yourcompany.android.tictactoe.domain.model

import android.content.Context
import android.content.Context.MODE_PRIVATE

object PreferenceHelper {

    private const val PREF_NAME = "com.yourcompany.android.tictactoe.prefs"
    private const val KEY_USERNAME = "username"

    fun saveUsername(context: Context, username: String) {
        val prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        prefs.edit().putString(KEY_USERNAME, username).apply()
    }

    fun getUsername(context: Context): String {
        val prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        return prefs.getString(KEY_USERNAME, "") ?: ""
    }

    fun clearUsername(context: Context) {
        val prefs = context.getSharedPreferences(PREF_NAME, MODE_PRIVATE)
        prefs.edit().remove(KEY_USERNAME).apply()
    }
}
