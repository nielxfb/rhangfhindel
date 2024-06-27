package edu.bluejack23_2.rhangfhindel.viewmodels

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack23_2.rhangfhindel.models.Assistant
import edu.bluejack23_2.rhangfhindel.utils.SharedPrefManager

class HomeViewModel(context: Context) : ViewModel() {

    var assistant = MutableLiveData<Assistant>(SharedPrefManager.getAssistant(context))
    var assistantUsername = ""

    init {
        assistant.observeForever { assistant ->
            assistantUsername = assistant?.Username ?: ""
        }
    }

}