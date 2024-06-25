package edu.bluejack23_2.rhangfhindel.utils

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.util.Log
import edu.bluejack23_2.rhangfhindel.repositories.NotificationRepository
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class AlarmReceiver : BroadcastReceiver() {

    override fun onReceive(context: Context, intent: Intent) {
        Log.d("ALARM", "Successfully called for schedule")

        CoroutineScope(Dispatchers.IO).launch {
            try {
                NotificationRepository.sendNotification("testing", "mantap")
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}