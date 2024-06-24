package edu.bluejack23_2.rhangfhindel.viewmodels

import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack23_2.rhangfhindel.modules.responses.Detail
import edu.bluejack23_2.rhangfhindel.repositories.RoomRepository
import edu.bluejack23_2.rhangfhindel.utils.Coroutines
import kotlinx.coroutines.awaitAll
import okhttp3.internal.wait

class RoomTransactionViewModel : ViewModel() {
    private val roomRepository = RoomRepository()

    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val success = MutableLiveData<Boolean>()
    val roomTransactions = MutableLiveData<List<Detail>>()

    fun onLoad(fetchRang: Boolean) {
        errorMessage.value = ""
        Coroutines.main {
            isLoading.value = true
            try {
                val response = roomRepository.getRoomTransactions()

                if (fetchRang) {
                    roomTransactions.value = getAvailableRangs(response.Details
                        .filter { it.Campus == "ANGGREK" })
                } else {
                    roomTransactions.value = response.Details
                        .filter { it.Campus == "ANGGREK" }
                }

                Log.d("activerangxixi", "Ini dalam onload ${roomTransactions.value}")
                success.value = true
            } catch (e: Exception) {
                errorMessage.value = e.toString()
            }
            isLoading.value = false
        }
    }

    private fun getAvailableRangs(roomTransactions: List<Detail>): List<Detail> {
        val activeIndices = setOf(1, 3, 5, 7, 9, 11)
        val activeRangs = mutableListOf<Detail>()

        for (detail in roomTransactions) {
            val statusDetail = detail.StatusDetails

            val allIndicesEmpty = activeIndices.all { index ->
                index < statusDetail.size && statusDetail[index].isEmpty()
            }

            if (allIndicesEmpty) {
                activeRangs.add(detail)
            }
        }

        return activeRangs
    }

    fun getRoomTransactions() {
//        if (!onLoad()) return


    }

}