package edu.bluejack23_2.rhangfhindel.activities

import android.content.Intent
import android.os.Bundle
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
    private val assistantRepository = AssistantRepository()
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        init()
        setEvent()
        changeStatusBarColor()

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        viewModel.logOn(this, "HH23-2", "Vovo13245")

        // Observasi LiveData untuk mendapatkan hasil
        viewModel.assistant.observe(this, Observer { assistantList ->
            // Handle hasil yang diterima, update UI, dsb.
        })
    }

    override fun init() {
        usernameInput = findViewById(R.id.username_input)
        passwordInput = findViewById(R.id.password_input)
        loginButton = findViewById(R.id.login_button)
    }

    override fun setEvent() {
        loginButton.setOnClickListener {
            val username = usernameInput.text
            val password = passwordInput.text

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                redirect()
            }
        }
    }

    override fun changeStatusBarColor() {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, R.color.primary_color)
    }

    private fun redirect() {
        val intent = Intent(this, DashboardActivity::class.java)
        startActivity(intent)
        finish()
    }
}