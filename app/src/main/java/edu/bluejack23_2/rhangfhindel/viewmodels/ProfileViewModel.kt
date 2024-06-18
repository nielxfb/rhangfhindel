package edu.bluejack23_2.rhangfhindel.viewmodels

import android.content.Context
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack23_2.rhangfhindel.BuildConfig
import edu.bluejack23_2.rhangfhindel.utils.SharedPrefManager

class ProfileViewModel : ViewModel() {
    var profileURL: String = ""
    var initial: String = ""
    var name: String = ""

    fun init(context: Context) {
        profileURL =
            BuildConfig.API_BASE_URL + "Account/GetThumbnail?id=" + SharedPrefManager.getAssistant(
                context
            )!!.PictureId
        initial = SharedPrefManager.getAssistant(context)!!.Username
        name = SharedPrefManager.getAssistant(context)!!.Name
    }
}