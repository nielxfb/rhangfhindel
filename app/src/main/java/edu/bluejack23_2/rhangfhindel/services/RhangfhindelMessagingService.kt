package edu.bluejack23_2.rhangfhindel.services

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import android.widget.RemoteViews
import androidx.core.app.NotificationCompat
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessagingService
import com.google.firebase.messaging.RemoteMessage
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.activities.LoginActivity
import edu.bluejack23_2.rhangfhindel.factories.LoginViewModelFactory
import edu.bluejack23_2.rhangfhindel.repositories.NotificationRepository
import edu.bluejack23_2.rhangfhindel.utils.Coroutines
import edu.bluejack23_2.rhangfhindel.utils.SharedPrefManager
import edu.bluejack23_2.rhangfhindel.viewmodels.LoginViewModel

class RhangfhindelMessagingService : FirebaseMessagingService() {

    override fun onNewToken(token: String) {
        super.onNewToken(token)

        val assistant = SharedPrefManager.getAssistant(applicationContext)

        if (assistant == null) {
            Log.e("FCM", "Assistant not logged in")
            return
        }

        val initial = assistant.Username
        val generation = initial.substring(2)

        Coroutines.main {
            try {
                NotificationRepository.saveToken(token, generation)
            } catch (exception: Exception) {
                Log.e("FCM", "Token missing")
                return@main
            }

            Log.d("FCM", "Token $token successfully saved")
        }

    }

    override fun onMessageReceived(message: RemoteMessage) {
        Log.d("FCM", "Message received from: ${message.from}")
        if (message.notification != null) {
            Log.d("FCM", "Notification Title: ${message.notification!!.title}")
            Log.d("FCM", "Notification Body: ${message.notification!!.body}")
            generateNotification(message.notification!!.title!!, message.notification!!.body!!)
        } else {
            Log.d("FCM", "Notification null")
        }
    }

    private fun getRemoteView(title: String, description: String): RemoteViews {
        val remoteView =
            RemoteViews("edu.bluejack23_2.rhangfhindel", R.layout.notification)
        remoteView.setTextViewText(R.id.title, title)
        remoteView.setTextViewText(R.id.description, description)
        remoteView.setImageViewResource(R.id.app_logo, R.mipmap.ic_launcher_round)
        return remoteView
    }

    @SuppressLint("UnspecifiedImmutableFlag")
    fun generateNotification(title: String, description: String) {
        Log.d("FCM", "Generating notification with title: $title and description: $description")
        createNotificationChannel()

        val intent = Intent(this, LoginActivity::class.java)
        intent.addFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP)

        val pendingIntent = PendingIntent.getActivity(this, 0, intent, PendingIntent.FLAG_IMMUTABLE)

        var builder: NotificationCompat.Builder = NotificationCompat.Builder(
            applicationContext,
            getString(R.string.notification_channel_id)
        )
            .setSmallIcon(R.mipmap.ic_launcher_round)
            .setAutoCancel(true)
            .setVibrate(longArrayOf(300, 100, 100))
            .setOnlyAlertOnce(true)
            .setContentIntent(pendingIntent)

        builder = builder.setContent(getRemoteView(title, description))

        val notificationManager =
            getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        Log.d("FCM", "Setup completed")
        notificationManager.notify(0, builder.build())
        Log.d("FCM", "Notif display")
    }

    private fun createNotificationChannel() {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channelId = getString(R.string.notification_channel_id)
            val channelName = getString(R.string.notification_channel_name)
            val importance = NotificationManager.IMPORTANCE_HIGH
            val channel = NotificationChannel(channelId, channelName, importance).apply {
                description = "Channel description"
            }
            val notificationManager: NotificationManager =
                getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
            notificationManager.createNotificationChannel(channel)
        }
    }


}