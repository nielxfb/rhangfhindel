package edu.bluejack23_2.rhangfhindel.utils

import android.util.Log
import edu.bluejack23_2.rhangfhindel.models.Detail
import edu.bluejack23_2.rhangfhindel.repositories.RoomRepository

object SharedRepository {

    var allRangs = listOf<Detail>()
    var allAlternatives = listOf<Detail>()

    fun onLoad() {
        Coroutines.main {
            try {
                val response = RoomRepository.getRoomTransactions()

                Log.d("Fetching", "Ini response: ${response.Details}")

                allRangs = getAvailableRangs(response.Details.filter {
                    it.Campus == "ANGGREK" && !it.RoomName.contains("724")
                })


                allAlternatives =
                    getAlternativeRangs(response.Details.filter { it.Campus == "ANGGREK" })

                Log.d("Fetching", "Selesai fetch")

            } catch (e: Exception) {
                Log.d("RANGS", e.toString())
            }
        }
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

        return alternativeRangs
    }

}