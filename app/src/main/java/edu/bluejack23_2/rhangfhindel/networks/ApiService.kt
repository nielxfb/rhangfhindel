package edu.bluejack23_2.rhangfhindel.networks

import edu.bluejack23_2.rhangfhindel.models.Assistant
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ApiService {
    @GET("Assistant")
    fun getAssistant(
        @Query("initial") initial: String,
        @Query("generation") generation: String
    ): Call<List<Assistant>>


}