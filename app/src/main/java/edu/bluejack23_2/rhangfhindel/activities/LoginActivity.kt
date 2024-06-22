package edu.bluejack23_2.rhangfhindel.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import edu.bluejack23_2.rhangfhindel.R
import android.widget.Button
import android.widget.EditText
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.bluejack23_2.rhangfhindel.base.BaseActivity
import edu.bluejack23_2.rhangfhindel.databinding.ActivityLoginBinding
import edu.bluejack23_2.rhangfhindel.utils.PopUp
import edu.bluejack23_2.rhangfhindel.viewmodels.LoginViewModel
import edu.bluejack23_2.rhangfhindel.viewmodels.RoomTransactionViewModel

class LoginActivity : BaseActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        val roomViewModel = RoomTransactionViewModel()
        roomViewModel.onLoad(fetchRang = true, fetchAlternatives = true)

        init()
        setEvent()
        changeStatusBarColor(R.color.primary_color)
    }

    override fun init() {
        usernameInput = findViewById(R.id.username_input)
        passwordInput = findViewById(R.id.password_input)
        loginButton = findViewById(R.id.login_button)
        viewModel = ViewModelProvider(this)[LoginViewModel::class.java]
        binding = DataBindingUtil.setContentView(this, R.layout.activity_login)
        binding.lifecycleOwner = this
        binding.viewmodel = viewModel
    }

    override fun setEvent() {
        binding.loginButton.setOnClickListener {
            viewModel.onLoginButtonClick(this)
        }

        viewModel.success.observe(this, Observer { success ->
            if (!success) {
                PopUp.shortDuration(this, "Credentials invalid!")
                return@Observer
            }

            PopUp.shortDuration(this, "Login success!")
            redirect()
        })

        viewModel.errorMessage.observe(this, Observer { message ->
            if (message.isNotEmpty()) {
                PopUp.shortDuration(this, message)
            }
        })

        viewModel.isLoading.observe(this, Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
        })
    }

    private fun redirect() {
        val intent = Intent(this, HomeActivity::class.java)
        startActivity(intent)
        finish()
    }
}