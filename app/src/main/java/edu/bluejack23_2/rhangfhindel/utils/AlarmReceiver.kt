package edu.bluejack23_2.rhangfhindel.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import edu.bluejack23_2.rhangfhindel.repositories.NotificationRepository
import edu.bluejack23_2.rhangfhindel.viewmodels.RoomTransactionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Coroutines.main {
            Log.d("ALARM", "Successfully called for schedule")

            val viewModel = RoomTransactionViewModel()
            viewModel.onLoad(fetchRang = true, fetchAlternatives = true, null, null, null)

            val rangs = viewModel.rangs.value
            val alternatives = viewModel.alternatives.value

            var rangStr = ""
            var alternativeStr = ""

            rangs?.forEach {
                rangStr += it.RoomName
                if (rangs.indexOf(it) != rangs.size - 1) {
                    rangStr += ", "
                }
            }

            alternatives?.forEach {
                alternativeStr += it.RoomName
                if (alternatives.indexOf(it) != alternatives.size - 1) {
                    alternativeStr += ", "
                }
            }

            try {
                NotificationRepository.sendNotification(
                    "Available rangs for today",
                    "Rang: ${rangStr}\nAlternatives: $alternativeStr",
                    ""
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}