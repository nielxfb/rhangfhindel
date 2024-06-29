package edu.bluejack23_2.rhangfhindel.repositories

import android.util.Log
import edu.bluejack23_2.rhangfhindel.modules.requests.NotificationRequest
import edu.bluejack23_2.rhangfhindel.modules.requests.PersonalNotificationRequest
import edu.bluejack23_2.rhangfhindel.modules.requests.SaveTokenRequest
import edu.bluejack23_2.rhangfhindel.modules.responses.SaveTokenResponse
import edu.bluejack23_2.rhangfhindel.networks.ServerRetrofitClient

object NotificationRepository {

    private lateinit var token: String

    suspend fun saveToken(token: String, generation: String): SaveTokenResponse {
        this.token = token
        Log.d("FCM", "$token di saveToken")
        val response =
            ServerRetrofitClient.getApiService().saveToken(SaveTokenRequest(token, generation))

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception(response.code().toString())
        }
    }

    suspend fun sendNotification(title: String, body: String, onlyForGeneration: String) {
        val response =
            ServerRetrofitClient.getApiService()
                .sendNotification(NotificationRequest(title, body, onlyForGeneration))

        if (response.isSuccessful) {
            Log.d("FCM", "Notification sent successfully")
        } else {
            throw Exception(response.code().toString())
        }
    }

    suspend fun sendPersonalNotification(title: String, body: String) {
        val response = ServerRetrofitClient.getApiService()
            .sendPersonalNotification(PersonalNotificationRequest(title, body, token))

        if (response.isSuccessful) {
            Log.d("FCM", "Personal notification sent successfully")
        } else {
            throw Exception(response.code().toString())
        }
    }

}