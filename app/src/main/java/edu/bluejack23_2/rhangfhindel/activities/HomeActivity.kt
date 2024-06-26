package edu.bluejack23_2.rhangfhindel.activities

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.messaging.FirebaseMessaging
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.base.BaseActivity
import edu.bluejack23_2.rhangfhindel.factories.LoginViewModelFactory
import edu.bluejack23_2.rhangfhindel.repositories.NotificationRepository
import edu.bluejack23_2.rhangfhindel.utils.Coroutines
import edu.bluejack23_2.rhangfhindel.viewmodels.LoginViewModel

class HomeActivity : BaseActivity() {

    private lateinit var loginViewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        changeStatusBarColor(R.color.transparent_background)
        init()
        saveToken()
    }

    override fun init() {
        val navController = findNavController(R.id.curr_fragment)
        val navbarView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navbarView.setupWithNavController(navController)
        val factory = LoginViewModelFactory(this)
        loginViewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]
    }

    private fun saveToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("FCM", "Fetching FCM token failed")
                return@addOnCompleteListener
            }

            val token = task.result
            val initial = loginViewModel.assistantUsername.value
            if (initial == null) {
                Log.e("FCM", "Assistant not logged in")
                return@addOnCompleteListener
            }

            val generation = initial.substring(2)
            Log.d("FCM", "Ini token di Login $token, generation $generation")

            Coroutines.main {
                try {
                    NotificationRepository.saveToken(token, generation)
                } catch (exception: Exception) {
                    Log.e("FCM", "Token missing di Login ${exception.message}")
                    return@main
                }

                Log.d("FCM", "Token successfuly uploaded")
            }
        }
    }

    override fun setEvent() {
        TODO("Not yet implemented")
    }
}