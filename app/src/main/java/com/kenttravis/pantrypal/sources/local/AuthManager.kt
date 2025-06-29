package com.kenttravis.pantrypal.sources.local

import android.content.Context

object AuthManager {
    private const val PREF_NAME = "auth_prefs"
    private const val TOKEN_KEY = "jwt_token"

    fun saveToken(context: Context, token: String) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPref.edit().putString(TOKEN_KEY, token).apply()
    }

    fun getToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(TOKEN_KEY, null)
    }

    fun hasToken(context: Context): Boolean {
        return getToken(context) != null
    }

    fun clearToken(context: Context) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        sharedPref.edit().remove(TOKEN_KEY).apply()
    }
}