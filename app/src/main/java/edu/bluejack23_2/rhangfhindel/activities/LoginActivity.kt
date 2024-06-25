package edu.bluejack23_2.rhangfhindel.activities

import android.os.Bundle
import androidx.lifecycle.ViewModelProvider
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.base.BaseActivity
import edu.bluejack23_2.rhangfhindel.databinding.ActivityLoginBinding
import edu.bluejack23_2.rhangfhindel.factories.LoginViewModelFactory
import edu.bluejack23_2.rhangfhindel.fragments.LoggedInFragment
import edu.bluejack23_2.rhangfhindel.fragments.LoginFragment
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

}
