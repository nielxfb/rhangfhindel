package edu.bluejack23_2.rhangfhindel.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.bluejack23_2.rhangfhindel.models.APIToken
import edu.bluejack23_2.rhangfhindel.models.Assistant
import edu.bluejack23_2.rhangfhindel.modules.LogOnBody
import edu.bluejack23_2.rhangfhindel.repository.AssistantRepository
import edu.bluejack23_2.rhangfhindel.utils.SharedPrefManager
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel(){
    private val assistantRepository = AssistantRepository()

    val assistant: LiveData<Assistant> get() = assistantRepository.assistant
    val apiToken: LiveData<APIToken> get() = assistantRepository.apiToken

    fun getMyIdentity(context: Context, initial: String, generation: String) {
        viewModelScope.launch {
            assistantRepository.getMyIdentity(context)
            Log.d("getMyIdentity", "didalam view model ${assistant.value!!.Username}")
        }
    }

    fun getApiToken(context: Context, username: String, password: String) {
        val logOnBody = LogOnBody(username, password)
        viewModelScope.launch {
            assistantRepository.getApiToken(context, logOnBody)
            Log.d("getApiToken", "didalam view model $apiToken")

            val token = apiToken.value?.access_token

            if(token != null){
                SharedPrefManager.saveAPIToken(context, token)
            }

            val sharedPrefToken = SharedPrefManager.getAPIToken(context)
            if (sharedPrefToken != null) {
                Log.i("sharedPref", sharedPrefToken)
            }
        }
    }
}