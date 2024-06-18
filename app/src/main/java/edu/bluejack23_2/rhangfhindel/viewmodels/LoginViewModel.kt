package edu.bluejack23_2.rhangfhindel.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack23_2.rhangfhindel.models.Assistant
import edu.bluejack23_2.rhangfhindel.modules.LogOnBody
import edu.bluejack23_2.rhangfhindel.repositories.AssistantRepository
import edu.bluejack23_2.rhangfhindel.utils.Coroutines
import edu.bluejack23_2.rhangfhindel.utils.SharedPrefManager
import java.io.IOException

class LoginViewModel : ViewModel() {
    private val assistantRepository = AssistantRepository()

    var username: String = ""
    var password: String = ""

    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val success = MutableLiveData<Boolean>()

    private fun validateInput(): Boolean {
        val initialPattern = "^[A-Za-z]{2}\\d{2}-\\d$".toRegex()

        if (username.isEmpty() || password.isEmpty()) {
            errorMessage.value = "All fields are requried"
        } else if (!username.matches(initialPattern)) {
            errorMessage.value = "Invalid initial format"
        }

        return errorMessage.value.isNullOrEmpty()
    }

    private fun saveAssistant(context: Context, assistant: Assistant) {
        assistant.let { SharedPrefManager.saveAssistant(context, it) }
    }

    fun onLoginButtonClick(context: Context) {
        if (!validateInput()) {
            return
        }

        val logOnBody = LogOnBody(username, password)
        Coroutines.main {
            isLoading.value = true
            try {
                val response = assistantRepository.logOn(logOnBody)
                success.value = true
                saveAssistant(context, response.User!!)
            } catch (e: IOException) {
                if (e.toString().contains("401")) {
                    errorMessage.value = "Invalid credentials"

                }
            }
            isLoading.value = false
        }
    }


}