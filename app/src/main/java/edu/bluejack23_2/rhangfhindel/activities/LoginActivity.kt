package edu.bluejack23_2.rhangfhindel.activities

import android.app.AlarmManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.lifecycleScope
import com.google.firebase.messaging.FirebaseMessaging
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.base.BaseActivity
import edu.bluejack23_2.rhangfhindel.databinding.ActivityLoginBinding
import edu.bluejack23_2.rhangfhindel.factories.LoginViewModelFactory
import edu.bluejack23_2.rhangfhindel.fragments.LoggedInFragment
import edu.bluejack23_2.rhangfhindel.fragments.LoginFragment
import edu.bluejack23_2.rhangfhindel.models.Detail
import edu.bluejack23_2.rhangfhindel.repositories.NotificationRepository
import edu.bluejack23_2.rhangfhindel.utils.AlarmReceiver
import edu.bluejack23_2.rhangfhindel.utils.Coroutines
import edu.bluejack23_2.rhangfhindel.viewmodels.LoginViewModel
import edu.bluejack23_2.rhangfhindel.viewmodels.RoomTransactionViewModel
import kotlinx.coroutines.launch
import java.util.Calendar

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var loginFragment: LoginFragment
    private lateinit var loggedInFragment: LoggedInFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        init()
        scheduleAlarm()

        setEvent()
        changeStatusBarColor(R.color.primary_color)
    }

    override fun init() {
        loginFragment = LoginFragment()
        loggedInFragment = LoggedInFragment()

        supportFragmentManager.beginTransaction().replace(R.id.login_container, loginFragment)
            .commit()

        initViewModel()

        viewModel.assistant.observeForever { assistant ->
            if (assistant == null) {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.login_container, loginFragment).commit()
            } else {
                supportFragmentManager.beginTransaction()
                    .replace(R.id.login_container, loggedInFragment).commit()
            }
        }
    }

    override fun setEvent() {}

    private fun initViewModel() {
        val factory = LoginViewModelFactory(this)
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    private fun scheduleAlarm() {
        val alarmManager = getSystemService(Context.ALARM_SERVICE) as AlarmManager

        val alarmIntent = Intent(this, AlarmReceiver::class.java)

        val pendingIntent = PendingIntent.getBroadcast(
            this,
            0,
            alarmIntent,
            PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE
        )

        val calendar = Calendar.getInstance().apply {
            timeInMillis = System.currentTimeMillis()
            set(Calendar.HOUR_OF_DAY, 11)
            set(Calendar.MINUTE, 26)
        }

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            calendar.timeInMillis,
            pendingIntent
        )
    }

}
