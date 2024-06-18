package edu.bluejack23_2.rhangfhindel.networks

import edu.bluejack23_2.rhangfhindel.models.APIToken
import edu.bluejack23_2.rhangfhindel.models.Assistant
import edu.bluejack23_2.rhangfhindel.modules.requests.LogOnRequest
import edu.bluejack23_2.rhangfhindel.modules.responses.LogOnResponse
import edu.bluejack23_2.rhangfhindel.modules.responses.RoomTransactionResponse
import retrofit2.Call
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Query
import java.util.Date

interface ApiService {

    @POST("Account/LogOnGalaxionRemake")
    suspend fun logOn(
        @Body logOnRequest: LogOnRequest
    ): Response<LogOnResponse>

    @GET("Room/GetTransactions")
    suspend fun getRoomTransactions(
        @Query("startDate") startDate: String,
        @Query("endDate") endDate: String,
        @Query("includeUnapproved") includeUnapproved: Boolean
    ): Response<RoomTransactionResponse>

}