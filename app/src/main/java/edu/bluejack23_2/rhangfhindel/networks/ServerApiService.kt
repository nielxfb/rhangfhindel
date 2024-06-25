package edu.bluejack23_2.rhangfhindel.networks

import edu.bluejack23_2.rhangfhindel.modules.requests.SaveTokenRequest
import edu.bluejack23_2.rhangfhindel.modules.responses.SaveTokenResponse
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.POST

interface ServerApiService {

    @POST("register-token")
    suspend fun saveToken(
        @Body token: SaveTokenRequest
    ): Response<SaveTokenResponse>

}