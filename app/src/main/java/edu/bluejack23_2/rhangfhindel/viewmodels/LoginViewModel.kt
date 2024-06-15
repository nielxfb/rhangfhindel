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

    fun logOn(username: String, password: String) {
        val logOnBody = LogOnBody(username, password)
        viewModelScope.launch {
            assistantRepository.logOn(logOnBody)
            Log.d("getMyIdentity", "didalam view model ${assistant.value}")
        }
    }

}