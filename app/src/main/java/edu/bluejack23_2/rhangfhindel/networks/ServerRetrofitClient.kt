package edu.bluejack23_2.rhangfhindel.networks

import edu.bluejack23_2.rhangfhindel.BuildConfig
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

object ServerRetrofitClient {

    private fun provideRetrofit(): Retrofit {
        val client = OkHttpClient.Builder()
            .build()

        return Retrofit.Builder()
            .baseUrl(BuildConfig.SERVER_BASE_URL) // TODO: Sesuain sama IP device saat ini. Nanti kalo udah dihost lebih gampang
            .client(client)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
    }

    fun getApiService(): ServerApiService {
        return provideRetrofit().create(ServerApiService::class.java)
    }

}