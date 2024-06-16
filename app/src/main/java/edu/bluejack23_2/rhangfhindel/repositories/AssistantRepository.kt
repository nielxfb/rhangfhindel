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
import java.io.IOException


class AssistantRepository {
    suspend fun logOn(logOnBody: LogOnBody): LogOnResponse {
        var response =  RetrofitClient.getApiService().logOn(logOnBody)

        if(response.isSuccessful){
            return response.body()!!
        }else{
            throw IOException(response.code().toString())
        }
    }
}