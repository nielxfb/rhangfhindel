package edu.bluejack23_2.rhangfhindel.viewmodels

import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack23_2.rhangfhindel.models.Assistant
import edu.bluejack23_2.rhangfhindel.models.Detail
import edu.bluejack23_2.rhangfhindel.repositories.FirebaseRepository
import edu.bluejack23_2.rhangfhindel.repositories.RoomRepository
import edu.bluejack23_2.rhangfhindel.utils.Coroutines
import edu.bluejack23_2.rhangfhindel.utils.SharedPrefManager

class HomeViewModel() : ViewModel() {

    var assistant = MutableLiveData<Assistant>()

    val errorMessage = MutableLiveData("")
    val isLoading = MutableLiveData(false)
    val success = MutableLiveData(false)
    val rangs = MutableLiveData<List<Detail>>().apply { value = listOf() }
    val activeRangs = MutableLiveData<List<Detail>>().apply { value = listOf() }
    var bookedRoomList = MutableLiveData<List<String>>().apply { value = listOf("") }
    val bookedInitialList = MutableLiveData<List<String>>().apply { value = listOf("") }
    var redirectRoom = MutableLiveData<Detail?>()

    var message = MutableLiveData<String>()

    var allRangs = listOf<Detail>()

    lateinit var context: Context

    fun onLoad(
        context: Context
    ) {
        redirectRoom.value = null


        assistant.value = SharedPrefManager.getAssistant(context)

        Coroutines.main {
            errorMessage.value = ""
            isLoading.value = true
            try {
                val response = RoomRepository.getRoomTransactions()

                allRangs = getAvailableRangs(response.Details
                    .filter { it.Campus == "ANGGREK" && !it.RoomName.contains("724") })
                rangs.value = allRangs


                Log.d("Fetching", "Selesai fetch")

                if (context != null) {
                    this.context = context
                    FirebaseRepository.getBookers(context) { rooms, initials ->
                        bookedRoomList.value = rooms
                        bookedInitialList.value = initials
                    }
                }

                success.value = true
            } catch (e: Exception) {
                errorMessage.value = e.toString()
            }

            getActiveRangs()

            isLoading.value = false
        }
    }

    private fun getAvailableRangs(roomTransactions: List<Detail>): List<Detail> {
        val activeIndices = setOf(1, 3, 5, 7, 9, 11)
        val activeRangs = mutableListOf<Detail>()

        for (detail in roomTransactions) {
            val statusDetail = detail.StatusDetails

            val allIndicesEmpty = activeIndices.all { index ->
                index < statusDetail.size &&
                        (statusDetail[index].isEmpty()
                                || statusDetail[index][0].Status == "C")
            }

            if (allIndicesEmpty) {
                activeRangs.add(detail)
            }
        }

        return activeRangs
    }

    fun getActiveRangs() {
        activeRangs.value = rangs.value?.filter { bookedRoomList.value!!.contains(it.RoomName) }
    }
}