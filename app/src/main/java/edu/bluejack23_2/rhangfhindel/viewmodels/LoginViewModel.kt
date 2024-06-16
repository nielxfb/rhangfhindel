package edu.bluejack23_2.rhangfhindel.viewmodels

import android.content.Context
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.bluejack23_2.rhangfhindel.models.APIToken
import edu.bluejack23_2.rhangfhindel.models.Assistant
import edu.bluejack23_2.rhangfhindel.modules.LogOnBody
import edu.bluejack23_2.rhangfhindel.repository.AssistantRepository
import edu.bluejack23_2.rhangfhindel.utils.SharedPrefManager
import kotlinx.coroutines.launch

class LoginViewModel : ViewModel() {
    private val assistantRepository = AssistantRepository()

    var username: String = ""
    var password: String = ""

    private val assistant: LiveData<Assistant> get() = assistantRepository.assistant

    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val success = MutableLiveData<Boolean>()

    private fun validateInput() : Boolean{
        val initialPattern = "^[A-Za-z]{2}\\d{2}-\\d$".toRegex()

        if (username.isEmpty() || password.isEmpty()) {
            errorMessage.value = "All fields are requried"
            return false
        } else if (!username.matches(initialPattern)) {
            errorMessage.value = "Invalid initial format"
            return false
        }
        return true
    }

    private fun saveAssistant(context: Context) {
        assistant.value?.let { SharedPrefManager.saveAssistant(context, it) }
    }
    fun onLoginButtonClick(context: Context){
        if(!validateInput()){
            return
        }

        val logOnBody = LogOnBody(username, password)
        viewModelScope.launch {
            isLoading.value = true
            success.value = assistantRepository.logOn(logOnBody)
            if(success.value == true){
                saveAssistant(context)
            }
            isLoading.value = false
        }
    }




}