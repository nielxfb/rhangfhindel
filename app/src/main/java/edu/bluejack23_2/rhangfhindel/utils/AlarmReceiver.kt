package edu.bluejack23_2.rhangfhindel.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import edu.bluejack23_2.rhangfhindel.repositories.NotificationRepository
import edu.bluejack23_2.rhangfhindel.viewmodels.RoomTransactionViewModel
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        CoroutineScope(Dispatchers.Main).launch {
            Log.d("ALARM", "Successfully called for schedule")

            val rangs = SharedRepository.allRangs
            val alternatives = SharedRepository.allAlternatives

            var rangStr = ""
            var alternativeStr = ""

            rangs.forEach {
                rangStr += it.RoomName
                if (rangs.indexOf(it) != rangs.size - 1) {
                    rangStr += ", "
                }
            }

            alternatives.forEach {
                alternativeStr += it.RoomName
                if (alternatives.indexOf(it) != alternatives.size - 1) {
                    alternativeStr += ", "
                }
            }

            try {
                NotificationRepository.sendPersonalNotification(
                    "Available rangs for today",
                    "Rang: ${rangStr}\nAlternatives: $alternativeStr",
                )
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}