package edu.bluejack23_2.rhangfhindel.viewmodels

import android.app.Dialog
import android.content.Context
import android.util.Log
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.databinding.BookModalBinding
import edu.bluejack23_2.rhangfhindel.databinding.FilterModalBinding
import edu.bluejack23_2.rhangfhindel.models.Detail
import edu.bluejack23_2.rhangfhindel.repositories.FirebaseRepository
import edu.bluejack23_2.rhangfhindel.repositories.RoomRepository
import edu.bluejack23_2.rhangfhindel.utils.Coroutines
import edu.bluejack23_2.rhangfhindel.utils.SharedPrefManager
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class RoomTransactionViewModel : ViewModel() {

    val errorMessage = MutableLiveData<String>()
    val isLoading = MutableLiveData<Boolean>()
    val success = MutableLiveData<Boolean>()
    val roomTransactions = MutableLiveData<List<Detail>>()
    val rangs = MutableLiveData<List<Detail>>()
    val activeRangs = MutableLiveData<List<Detail>>()
    var bookedRoomList = MutableLiveData<List<String>>().apply { value = listOf("") }
    val bookedInitialList = MutableLiveData<List<String>>().apply { value = listOf("") }
    val alternatives = MutableLiveData<List<Detail>>()
    var redirectRoom = MutableLiveData<Detail?>()
    var message = MutableLiveData<String>()

    var allRangs = listOf<Detail>()
    var allRooms = listOf<Detail>()
    var allAlternatives = listOf<Detail>()

    lateinit var bookModal: Dialog
    lateinit var bookModalBinding: BookModalBinding
    lateinit var filterModal: Dialog
    lateinit var filterModalBinding: FilterModalBinding

    lateinit var currRoom: Detail
    lateinit var context: Context

    var searchQuery = MutableLiveData<String>()

    fun onLoad(
        fetchRang: Boolean,
        fetchAlternatives: Boolean,
        bookModal: Dialog?,
        bookModalBinding: BookModalBinding?,
        filterModal: Dialog?,
        filterModalBinding: FilterModalBinding?,
        context: Context?
    ): Boolean {
        redirectRoom.value = null

        Coroutines.main {

            if (bookModal != null) {
                this.bookModal = bookModal
            }
            if (bookModalBinding != null) {
                this.bookModalBinding = bookModalBinding
            }
            if (filterModal != null) {
                this.filterModal = filterModal
            }
            if (filterModalBinding != null) {
                this.filterModalBinding = filterModalBinding
            }

            errorMessage.value = ""
            isLoading.value = true
            try {
                val response = RoomRepository.getRoomTransactions()

                Log.d("Fetching", "Ini response: ${response.Details}")

                if (fetchRang) {
                    allRangs = getAvailableRangs(response.Details.filter {
                        it.Campus == "ANGGREK" && !it.RoomName.contains("724")
                    })
                    rangs.value = allRangs
                } else {
                    allRooms =
                        response.Details.filter { it.Campus == "ANGGREK" && !it.RoomName.contains("724") }
                    roomTransactions.value = allRooms
                }


                if (fetchAlternatives) {
                    allAlternatives =
                        getAlternativeRangs(response.Details.filter { it.Campus == "ANGGREK" })
                    alternatives.value = allAlternatives
                }

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
            isLoading.value = false
        }

        return errorMessage.value == ""
    }

    private fun getAvailableRangs(roomTransactions: List<Detail>): List<Detail> {
        val activeIndices = setOf(1, 3, 5, 7, 9, 11)
        val activeRangs = mutableListOf<Detail>()

        for (detail in roomTransactions) {
            val statusDetail = detail.StatusDetails

            val allIndicesEmpty = activeIndices.all { index ->
                index < statusDetail.size && (statusDetail[index].isEmpty() || statusDetail[index][0].Status == "C")
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
                )) || (statusDetail[3].isNotEmpty() && statusDetail[3][0].Status != "C" && areFollowingIndicesValid(
                    5
                )) || (statusDetail[5].isNotEmpty() && statusDetail[5][0].Status != "C" && areFollowingIndicesValid(
                    7
                ))

            if (isValidRang) {
                alternativeRangs.add(detail)
            }
        }

        getActiveRangs()

        return alternativeRangs
    }

    fun getActiveRangs() {
        activeRangs.value = rangs.value?.filter { bookedRoomList.value!!.contains(it.RoomName) }
    }

    fun showRangModal(roomName: String) {
        bookModalBinding.textViewDialog.text =
            String.format(context.getString(R.string.booking_confirmation), roomName)
        bookModal.show()
    }

    fun showFilterModal() {
        filterModal.show()
    }

    fun closeBookModal() {
        bookModal.dismiss()
    }

    fun onApplyFilterRang(is6Floor: Boolean, is7Floor: Boolean) {
        rangs.value = allRangs
        rangs.value = rangs.value!!.filter { detail ->
            if ((is6Floor && is7Floor) || (!is6Floor && !is7Floor)) {
                return@filter true
            } else if (is6Floor) {
                return@filter detail.RoomName.startsWith("6")
            } else if (is7Floor) {
                return@filter detail.RoomName.startsWith("7")
            }
            return@filter false
        }

        alternatives.value = allAlternatives
        alternatives.value = alternatives.value!!.filter { detail ->
            if ((is6Floor && is7Floor) || (!is6Floor && !is7Floor)) {
                return@filter true
            } else if (is6Floor) {
                return@filter detail.RoomName.startsWith("6")
            } else if (is7Floor) {
                return@filter detail.RoomName.startsWith("7")
            }
            return@filter false
        }
        filterModal.dismiss()
    }

    fun onApplyFilterRoom(is6Floor: Boolean, is7Floor: Boolean) {
        roomTransactions.value = allRooms
        roomTransactions.value = roomTransactions.value!!.filter { detail ->
            if ((is6Floor && is7Floor) || (!is6Floor && !is7Floor)) {
                return@filter true
            } else if (is6Floor) {
                return@filter detail.RoomName.startsWith("6")
            } else if (is7Floor) {
                return@filter detail.RoomName.startsWith("7")
            }
            return@filter false
        }
        filterModal.dismiss()
    }

    fun bookRang(context: Context) {
        Coroutines.main {
            isLoading.value = true
            FirebaseRepository.addBooker(
                currRoom.RoomName, SharedPrefManager.getAssistant(context)!!.Username
            )
            isLoading.value = false
            closeBookModal()
            message.value = "Successfully Booked Room ${currRoom.RoomName}"
        }
    }

}