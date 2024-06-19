package edu.bluejack23_2.rhangfhindel.repositories

import edu.bluejack23_2.rhangfhindel.modules.responses.RoomTransactionResponse
import edu.bluejack23_2.rhangfhindel.networks.RetrofitClient
import okio.IOException
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date

class RoomRepository {

    suspend fun getRoomTransactions(): RoomTransactionResponse {
        val response = RetrofitClient.getApiService().getRoomTransactions(
            LocalDate.now().format(DateTimeFormatter.ISO_DATE),
            LocalDate.now().format(DateTimeFormatter.ISO_DATE),
            true
        )

        if (response.isSuccessful) {
            return response.body()!!
        } else {
            throw Exception(response.code().toString())
        }
    }

}