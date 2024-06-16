package edu.bluejack23_2.rhangfhindel.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.bluejack23_2.rhangfhindel.models.Assistant
import edu.bluejack23_2.rhangfhindel.modules.LogOnBody
import edu.bluejack23_2.rhangfhindel.modules.responses.LogOnResponse
import edu.bluejack23_2.rhangfhindel.networks.ApiService
import edu.bluejack23_2.rhangfhindel.networks.RetrofitClient
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import retrofit2.Response


class AssistantRepository {

    private val _assistant = MutableLiveData<Assistant>()
    val assistant: LiveData<Assistant> get() = _assistant

    suspend fun logOn(logOnBody: LogOnBody): Response<LogOnResponse> {
//        var success: Boolean
//        withContext(Dispatchers.IO) {
//            val call = RetrofitClient.getApiService().logOn(logOnBody)
//            val response = call.execute()
//            if (!response.isSuccessful) {
//                success = false
//                return@withContext
//            }
//
//            _assistant.postValue(response.body()?.User)
//            success = true
//        }
//        return success
        return RetrofitClient.getApiService().logOn(logOnBody)
    }
}