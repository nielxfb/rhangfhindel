package edu.bluejack23_2.rhangfhindel.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.bluejack23_2.rhangfhindel.models.Assistant
import edu.bluejack23_2.rhangfhindel.modules.LogOnBody
import edu.bluejack23_2.rhangfhindel.networks.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext


class AssistantRepository {

    private val _assistant = MutableLiveData<Assistant>()
    val assistant: LiveData<Assistant> get() = _assistant

    suspend fun logOn(context: Context, logOnBody: LogOnBody) {
        withContext(Dispatchers.IO) {
            val call = RetrofitClient.getApiService(context).logOn(logOnBody)
            val response = call.execute()
            if (response.isSuccessful) {
                _assistant.postValue(response.body()?.User)
                Log.d("getApiToken", "Data received: ${response.body()}")

            } else {
                Log.e("getApiToken", "Request failed: ${response.errorBody()?.string()}")
            }
        }
    }
}