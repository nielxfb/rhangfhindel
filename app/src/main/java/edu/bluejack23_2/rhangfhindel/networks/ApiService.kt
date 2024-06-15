package edu.bluejack23_2.rhangfhindel.networks

import edu.bluejack23_2.rhangfhindel.models.APIToken
import edu.bluejack23_2.rhangfhindel.models.Assistant
import edu.bluejack23_2.rhangfhindel.modules.LogOnBody
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query

interface ApiService {
    @GET("Account/Me")
    fun getMyIdentity(
    ): Call<Assistant>

    @POST("Account/LogOn")
    fun logOn(
        @Body logOnBody: LogOnBody
    ): Call<APIToken>


}