package edu.bluejack23_2.rhangfhindel.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack23_2.rhangfhindel.models.Assistant
import edu.bluejack23_2.rhangfhindel.modules.requests.LogOnRequest
import edu.bluejack23_2.rhangfhindel.repositories.AssistantRepository
import edu.bluejack23_2.rhangfhindel.utils.Coroutines
import edu.bluejack23_2.rhangfhindel.utils.SharedPrefManager
import java.io.IOException

class LoginViewModel(private val context: Context) : ViewModel() {
    var username: String = ""
    var password: String = ""

    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val success = MutableLiveData<Boolean>()
    val assistant = MutableLiveData<Assistant>(SharedPrefManager.getAssistant(context))

    val assistantUsername = MutableLiveData<String>()

    init {
        assistant.observeForever { assistant ->
            assistantUsername.value = assistant?.Username ?: ""
        }
    }

    private fun validateInput(): Boolean {
        errorMessage.value = ""
        val initialPattern = "^[A-Za-z]{2}\\d{2}-\\d$".toRegex()

        if (username.isEmpty() || password.isEmpty()) {
            errorMessage.value = "All fields are required"
        } else if (!username.matches(initialPattern)) {
            errorMessage.value = "Invalid initial format"
        }

        return errorMessage.value.isNullOrEmpty()
    }

    private fun saveAssistant(assistant: Assistant) {
        assistant.let { SharedPrefManager.saveAssistant(context, it) }
    }

    fun onLoginButtonClick() {
        if (!validateInput()) {
            return
        }

        val logOnRequest = LogOnRequest(username, password)
        Coroutines.main {
            isLoading.value = true
            try {
                val response = AssistantRepository.logOn(logOnRequest)
                success.value = true
                saveAssistant(response.User!!)
            } catch (e: IOException) {
                if (e.toString().contains("401")) {
                    errorMessage.value = "Invalid credentials"
                }
            }
            isLoading.value = false
        }
    }


}