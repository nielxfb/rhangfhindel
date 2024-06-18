package edu.bluejack23_2.rhangfhindel.repositories

import edu.bluejack23_2.rhangfhindel.modules.requests.LogOnRequest
import edu.bluejack23_2.rhangfhindel.modules.responses.LogOnResponse
import edu.bluejack23_2.rhangfhindel.networks.RetrofitClient
import java.io.IOException

class AssistantRepository {
    suspend fun logOn(logOnRequest: LogOnRequest): LogOnResponse {
        val response = RetrofitClient.getApiService().logOn(logOnRequest)

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw IOException(response.code().toString())
        }
    }
}