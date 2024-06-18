package edu.bluejack23_2.rhangfhindel.activities

import android.content.Intent
import android.os.Bundle
import android.view.View
import edu.bluejack23_2.rhangfhindel.R
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.bluejack23_2.rhangfhindel.base.BaseActivity
import edu.bluejack23_2.rhangfhindel.databinding.ActivityLoginBinding
import edu.bluejack23_2.rhangfhindel.viewmodels.LoginViewModel

class LoginActivity : BaseActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
    private lateinit var binding: ActivityLoginBinding
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

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
        binding.loginButton.setOnClickListener{
            viewModel.onLoginButtonClick(this)
        }

        viewModel.success.observe(this, Observer { success ->
            if(!success){
                Toast.makeText(this, "Credentials invalid", Toast.LENGTH_SHORT).show()
                return@Observer
            }

            redirect()
        })

        viewModel.errorMessage.observe(this, Observer { message ->
            Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
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