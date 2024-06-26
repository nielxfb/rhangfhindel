package edu.bluejack23_2.rhangfhindel.viewmodels

import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import edu.bluejack23_2.rhangfhindel.activities.RoomDetailActivity
import edu.bluejack23_2.rhangfhindel.databinding.BookModalBinding
import edu.bluejack23_2.rhangfhindel.models.Detail
import edu.bluejack23_2.rhangfhindel.models.StatusDetail
import edu.bluejack23_2.rhangfhindel.repositories.RoomRepository
import edu.bluejack23_2.rhangfhindel.utils.Coroutines
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext

class RoomTransactionViewModel : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val success = MutableLiveData<Boolean>()
    val roomTransactions = MutableLiveData<List<Detail>>()
    val rangs = MutableLiveData<List<Detail>>()
    val alternatives = MutableLiveData<List<Detail>>()
    var redirectRoom = MutableLiveData<Detail?>()

    lateinit var modal: Dialog
    lateinit var modalBinding: BookModalBinding


    fun onLoad(
        fetchRang: Boolean,
        fetchAlternatives: Boolean,
        modal: Dialog?,
        modalBinding: BookModalBinding?,
    ) {

        redirectRoom.value = null

        if (modal != null) {
            this.modal = modal
        }
        if (modalBinding != null) {
            this.modalBinding = modalBinding
        }

        Coroutines.main {
            errorMessage.value = ""
            isLoading.value = true
            try {
                val response = RoomRepository.getRoomTransactions()

                Log.d("Fetching", "Ini response: ${response.Details}")

                if (fetchRang) {
                    rangs.value = getAvailableRangs(response.Details
                        .filter { it.Campus == "ANGGREK" && !it.RoomName.contains("724") })
                } else {
                    roomTransactions.value = response.Details
                        .filter { it.Campus == "ANGGREK" && !it.RoomName.contains("724") }
                }

                if (fetchAlternatives) {
                    alternatives.value = getAlternativeRangs(response.Details
                        .filter { it.Campus == "ANGGREK" })
                }

                Log.d("Fetching", "Selesai fetch")

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

    private fun getAlternativeRangs(roomTransactions: List<Detail>): List<Detail> {
        val alternativeRangs = mutableListOf<Detail>()

        for (detail in roomTransactions) {
            val statusDetail = detail.StatusDetails

            fun areFollowingIndicesValid(startIndex: Int): Boolean {
                val oddIndices = listOf(1, 3, 5, 7, 9, 11)
                val startPosition = oddIndices.indexOf(startIndex)

                for (i in startPosition until oddIndices.size) {
                    val index = oddIndices[i]
                    if (!(statusDetail[index].isEmpty() || statusDetail[index][0].Status == "C")) {
                        return false
                    }
                }
                return true
            }

            val isValidRang =
                (statusDetail[1].isNotEmpty() && statusDetail[1][0].Status != "C" && areFollowingIndicesValid(
                    3
                )) ||
                        (statusDetail[3].isNotEmpty() && statusDetail[3][0].Status != "C" && areFollowingIndicesValid(
                            5
                        )) ||
                        (statusDetail[5].isNotEmpty() && statusDetail[5][0].Status != "C" && areFollowingIndicesValid(
                            7
                        ))

            if (isValidRang) {
                alternativeRangs.add(detail)
            }
        }

        return alternativeRangs
    }

    fun showRangModal(roomName: String) {
        modalBinding.textViewDialog.text = "Do you want to book Room ${roomName}?"
        modal.show()
    }

    fun closeModal() {
        modal.dismiss()
    }

}