package edu.bluejack23_2.rhangfhindel.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.bluejack23_2.rhangfhindel.models.APIToken
import edu.bluejack23_2.rhangfhindel.models.Assistant
import edu.bluejack23_2.rhangfhindel.modules.LogOnBody
import edu.bluejack23_2.rhangfhindel.networks.RetrofitClient
import edu.bluejack23_2.rhangfhindel.utils.SharedPrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response


class AssistantRepository {

    private val _assistant = MutableLiveData<Assistant>()
    val assistant: LiveData<Assistant> get() = _assistant

    private val _apiToken = MutableLiveData<APIToken>()
    val apiToken: LiveData<APIToken> get() = _apiToken

    suspend fun getMyIdentity(context: Context) {
        withContext(Dispatchers.IO) {
            val call = RetrofitClient.getApiService(context).getMyIdentity()
            val response = call.execute()
            if (response.isSuccessful) {
                _assistant.postValue(response.body())
                Log.d("getMyIdentity", "Data received: ${response.body()}")
            } else {
                Log.e("getMyIdentity", "Request failed: ${response.errorBody()?.string()}")
            }
        }
    }

    suspend fun getApiToken(context: Context, logOnBody: LogOnBody) {
        withContext(Dispatchers.IO) {
            val call = RetrofitClient.getApiService(context).logOn(logOnBody)
            val response = call.execute()
            if (response.isSuccessful) {
                _apiToken.postValue(response.body())
                Log.d("getApiToken", "Data received: ${response.body()}")

            } else {
                Log.e("getApiToken", "Request failed: ${response.errorBody()?.string()}")
            }
        }
    }
}