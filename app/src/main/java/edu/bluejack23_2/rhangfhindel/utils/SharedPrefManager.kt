package edu.bluejack23_2.rhangfhindel.utils

import android.content.Context

object SharedPrefManager {
    private const val PREF_NAME = "my_shared_preferences"
    private const val KEY_TOKEN = "api_token"

    fun saveAPIToken(context: Context, token: String) {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        with(sharedPref.edit()) {
            putString(KEY_TOKEN, token)
            apply()
        }
    }

    fun getAPIToken(context: Context): String? {
        val sharedPref = context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
        return sharedPref.getString(KEY_TOKEN, null)
    }
}