package edu.bluejack23_2.rhangfhindel.repositories

import edu.bluejack23_2.rhangfhindel.modules.LogOnBody
import edu.bluejack23_2.rhangfhindel.modules.responses.LogOnResponse
import edu.bluejack23_2.rhangfhindel.networks.RetrofitClient
import java.io.IOException

class AssistantRepository {
    suspend fun logOn(logOnBody: LogOnBody): LogOnResponse {
        val response = RetrofitClient.getApiService().logOn(logOnBody)

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw IOException(response.code().toString())
        }
    }
}