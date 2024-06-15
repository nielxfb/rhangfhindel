package edu.bluejack23_2.rhangfhindel.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import edu.bluejack23_2.rhangfhindel.R
import android.view.Window
import android.view.WindowManager
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.core.content.ContextCompat
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import edu.bluejack23_2.rhangfhindel.base.BaseActivity
import edu.bluejack23_2.rhangfhindel.repository.AssistantRepository
import edu.bluejack23_2.rhangfhindel.viewmodels.LoginViewModel

class LoginActivity : BaseActivity() {

    private lateinit var usernameInput: EditText
    private lateinit var passwordInput: EditText
    private lateinit var loginButton: Button
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
    }

    override fun setEvent() {
        loginButton.setOnClickListener {
            val username = usernameInput.text.toString()
            val password = passwordInput.text.toString()

            val initialPattern = "^[A-Za-z]{2}\\d{2}-\\d$".toRegex()

            var error = ""
            if (username.isEmpty() || password.isEmpty()) {
                error = "All fields are required"
            } else if (!username.matches(initialPattern)) {
                error = "Invalid initial format"
            }

            if (error.isNotEmpty()) {
                Toast.makeText(this, error, Toast.LENGTH_SHORT).show()
                return@setOnClickListener
            }

            viewModel.success.observe(this, Observer { success ->
                if (!success) {
                    Toast.makeText(this, "Credentials invalid", Toast.LENGTH_SHORT).show()
                    return@Observer
                }

                redirect()
            })

            viewModel.logOn(this, username, password)
        }
    }

    private fun redirect() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}