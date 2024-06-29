package edu.bluejack23_2.rhangfhindel.utils

import android.content.Context
import android.content.SharedPreferences
import android.util.Log
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.gson.Gson
import edu.bluejack23_2.rhangfhindel.BuildConfig
import edu.bluejack23_2.rhangfhindel.models.Assistant

object SharedPrefManager {
    private fun getEncryptedSharedPreferences(context: Context): SharedPreferences {
        val masterKeyAlias = MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC)
        return EncryptedSharedPreferences.create(
            BuildConfig.SHARED_PREF_KEY,
            masterKeyAlias,
            context,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    fun saveAssistant(context: Context, assistant: Assistant) {
        val sharedPreferences = getEncryptedSharedPreferences(context)
        val editor = sharedPreferences.edit()
        val gson = Gson()
        val assistantJson = gson.toJson(assistant)
        editor.putString(BuildConfig.ASSISTANT_KEY, assistantJson)
        editor.apply()
    }

    fun getAssistant(context: Context): Assistant? {
        val sharedPreferences = getEncryptedSharedPreferences(context)
        val gson = Gson()
        val assistantJson = sharedPreferences.getString(BuildConfig.ASSISTANT_KEY, null)
        return gson.fromJson(assistantJson, Assistant::class.java)
    }

    fun clearAssistant(context: Context) {
        val sharedPreferences = getEncryptedSharedPreferences(context)
        val editor = sharedPreferences.edit()
        editor.remove(BuildConfig.ASSISTANT_KEY)
        editor.apply()
    }
}