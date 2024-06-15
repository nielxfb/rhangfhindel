package edu.bluejack23_2.rhangfhindel.utils

import android.content.Context
import android.content.SharedPreferences
import com.google.gson.Gson
import edu.bluejack23_2.rhangfhindel.models.Assistant

object SharedPrefManager {
    // TODO: jangan lupa pindahin ke env
    private const val PREF_NAME = "my_shared_preferences"
    private const val KEY_ASSISTANT = "assistant"

    private fun getSharedPreferences(context: Context): SharedPreferences {
        return context.getSharedPreferences(PREF_NAME, Context.MODE_PRIVATE)
    }
    fun saveAssistant(context: Context, assistant: Assistant) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val assistantJson = gson.toJson(assistant)
        editor.putString(KEY_ASSISTANT, assistantJson)
        editor.apply()
    }

    fun getAssistant(context: Context): Assistant? {
        val sharedPreferences = getSharedPreferences(context)
        val gson = Gson()
        val assistantJson = sharedPreferences.getString(KEY_ASSISTANT, null)
        return gson.fromJson(assistantJson, Assistant::class.java)
    }

    fun clearAssistant(context: Context) {
        val sharedPreferences = getSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.remove(KEY_ASSISTANT)
        editor.apply()
    }


}