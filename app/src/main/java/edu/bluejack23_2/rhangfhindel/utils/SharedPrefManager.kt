package edu.bluejack23_2.rhangfhindel.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import com.google.gson.Gson
import edu.bluejack23_2.rhangfhindel.BuildConfig
import edu.bluejack23_2.rhangfhindel.models.Assistant

object SharedPrefManager {
    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(BuildConfig.SHARED_PREF_KEY, Context.MODE_PRIVATE)
    }

    fun saveAssistant(context: Context, assistant: Assistant) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val assistantJson = gson.toJson(assistant)
        editor.putString(BuildConfig.ASSISTANT_KEY, assistantJson)
        editor.apply()
    }

    fun getAssistant(context: Context): Assistant? {
        val sharedPreferences = getSharedPreferences(context)
        val gson = Gson()
        val assistantJson = sharedPreferences.getString(BuildConfig.ASSISTANT_KEY, null)
        return gson.fromJson(assistantJson, Assistant::class.java)
    }

    fun clearAssistant(context: Context) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.remove(BuildConfig.ASSISTANT_KEY)
        editor.apply()
    }

}