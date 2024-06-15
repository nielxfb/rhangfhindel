package edu.bluejack23_2.rhangfhindel.repository

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import edu.bluejack23_2.rhangfhindel.models.Assistant
import edu.bluejack23_2.rhangfhindel.networks.RetrofitClient
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AssistantRepository {

    private val _assistantList = MutableLiveData<List<Assistant>>()
    val assistantList: LiveData<List<Assistant>> get() = _assistantList

    fun fetchAssistantData(context: Context, initial: String, generation: String) {
        val call = RetrofitClient.getApiService(context).getAssistant(initial, generation)
        call.enqueue(object : Callback<List<Assistant>> {
            override fun onResponse(call: Call<List<Assistant>>, response: Response<List<Assistant>>) {
                Log.i("mantapsekali", "onResponse")
                if (response.isSuccessful) {
                    _assistantList.postValue(response.body())
                    Log.d("mantapsekali", "Data received: ${response.body()}")
                } else {
                    Log.e("mantapsekali", "Request failed: ${response.errorBody()?.string()}")
                }
            }

            override fun onFailure(call: Call<List<Assistant>>, t: Throwable) {
                Log.e("mantapsekali", "Error: ${t.message}", t)
            }
        })
    }
}