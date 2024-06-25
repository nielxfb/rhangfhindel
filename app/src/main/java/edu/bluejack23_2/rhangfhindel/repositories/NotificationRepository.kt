package edu.bluejack23_2.rhangfhindel.repositories

import android.util.Log
import edu.bluejack23_2.rhangfhindel.modules.requests.NotificationRequest
import edu.bluejack23_2.rhangfhindel.modules.requests.SaveTokenRequest
import edu.bluejack23_2.rhangfhindel.modules.responses.SaveTokenResponse
import edu.bluejack23_2.rhangfhindel.networks.ServerRetrofitClient

object NotificationRepository {

    suspend fun saveToken(token: String): SaveTokenResponse {
        Log.d("FCM", "$token di saveToken")
        val response = ServerRetrofitClient.getApiService().saveToken(SaveTokenRequest(token))

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception(response.code().toString())
        }
    }

    suspend fun sendNotification(title: String, body: String) {
        val response =
            ServerRetrofitClient.getApiService().sendNotification(NotificationRequest(title, body))

        if (response.isSuccessful) {
            Log.d("FCM", "Notification sent successfully")
        } else {
            throw Exception(response.code().toString())
        }
    }

}