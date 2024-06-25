package edu.bluejack23_2.rhangfhindel.activities

import android.os.Bundle
import android.util.Log
import androidx.lifecycle.ViewModelProvider
import com.google.firebase.messaging.FirebaseMessaging
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.base.BaseActivity
import edu.bluejack23_2.rhangfhindel.databinding.ActivityLoginBinding
import edu.bluejack23_2.rhangfhindel.factories.LoginViewModelFactory
import edu.bluejack23_2.rhangfhindel.fragments.LoggedInFragment
import edu.bluejack23_2.rhangfhindel.fragments.LoginFragment
import edu.bluejack23_2.rhangfhindel.repositories.TokenRepository
import edu.bluejack23_2.rhangfhindel.utils.Coroutines
import edu.bluejack23_2.rhangfhindel.viewmodels.LoginViewModel
import edu.bluejack23_2.rhangfhindel.viewmodels.RoomTransactionViewModel

class LoginActivity : BaseActivity() {

    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel
    private lateinit var loginFragment: LoginFragment
    private lateinit var loggedInFragment: LoggedInFragment

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)
        setContentView(binding.root)

        saveToken()

        init()

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

    private fun saveToken() {
        FirebaseMessaging.getInstance().token.addOnCompleteListener { task ->
            if (!task.isSuccessful) {
                Log.e("FCM", "Fetching FCM token failed")
                return@addOnCompleteListener
            }

            val token = task.result
            Log.d("FCM", "Ini token di Login $token")

            Coroutines.main {
                try {
                    TokenRepository.saveToken(token)
                } catch (exception: Exception) {
                    Log.e("FCM", "Token missing di Login ${exception.message}")
                    return@main
                }

                Log.d("FCM", "Token successfuly uploaded")
            }
        }
    }

}
