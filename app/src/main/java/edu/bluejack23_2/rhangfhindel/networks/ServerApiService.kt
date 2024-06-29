package edu.bluejack23_2.rhangfhindel.networks

import edu.bluejack23_2.rhangfhindel.modules.requests.NotificationRequest
import edu.bluejack23_2.rhangfhindel.modules.requests.PersonalNotificationRequest
import edu.bluejack23_2.rhangfhindel.modules.requests.SaveTokenRequest
import edu.bluejack23_2.rhangfhindel.modules.responses.NotificationResponse
import edu.bluejack23_2.rhangfhindel.modules.responses.SaveTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ServerApiService {

    @POST("register-token")
    suspend fun saveToken(
        @Body token: SaveTokenRequest
    ): Response<SaveTokenResponse>

    @POST("send-notification")
    suspend fun sendNotification(
        @Body notification: NotificationRequest
    ): Response<NotificationResponse>

    @POST("send-personal-notification")
    suspend fun sendPersonalNotification(
        @Body notification: PersonalNotificationRequest
    ): Response<NotificationResponse>

}