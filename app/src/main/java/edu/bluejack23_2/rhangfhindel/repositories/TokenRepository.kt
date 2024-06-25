package edu.bluejack23_2.rhangfhindel.repositories

import android.util.Log
import edu.bluejack23_2.rhangfhindel.modules.requests.SaveTokenRequest
import edu.bluejack23_2.rhangfhindel.modules.responses.SaveTokenResponse
import edu.bluejack23_2.rhangfhindel.networks.ServerRetrofitClient

object TokenRepository {

    suspend fun saveToken(token: String): SaveTokenResponse {
        Log.d("FCM", "$token di saveToken")
        val response = ServerRetrofitClient.getApiService().saveToken(SaveTokenRequest(token))

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception(response.code().toString())
        }
    }

}