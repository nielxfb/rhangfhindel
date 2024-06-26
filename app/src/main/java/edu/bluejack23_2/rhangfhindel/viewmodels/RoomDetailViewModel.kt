package edu.bluejack23_2.rhangfhindel.viewmodels

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.map
import edu.bluejack23_2.rhangfhindel.models.Detail

class RoomDetailViewModel : ViewModel() {

    var room = MutableLiveData<Detail>()
}