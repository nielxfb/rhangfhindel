package edu.bluejack23_2.rhangfhindel.activities

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
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
import edu.bluejack23_2.rhangfhindel.repository.AssistantRepository
import edu.bluejack23_2.rhangfhindel.viewmodels.LoginViewModel

class LoginActivity : AppCompatActivity() {

    private lateinit var username_input: EditText
    private lateinit var password_input: EditText
    private lateinit var login_button: Button
    private val assistantRepository = AssistantRepository()
    private lateinit var viewModel: LoginViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        init()
        setEvent()

        viewModel = ViewModelProvider(this).get(LoginViewModel::class.java)

        viewModel.getApiToken(this, "hh23-2", "Vovo13245")

        viewModel.getMyIdentity(this, "hh23-2", "23-2")


        // Observasi LiveData untuk mendapatkan hasil
        viewModel.assistant.observe(this, Observer { assistantList ->
            // Handle hasil yang diterima, update UI, dsb.
        })
    }

    private fun init() {
        changeStatusBarColor()

        username_input = findViewById(R.id.username_input)
        password_input = findViewById(R.id.password_input)
        login_button = findViewById(R.id.login_button)
    }

    private fun setEvent() {
        login_button.setOnClickListener {
            var username = username_input.text
            var password = password_input.text

            if (username.isEmpty() || password.isEmpty()) {
                Toast.makeText(this, "All fields are required", Toast.LENGTH_SHORT).show()
            } else {
                redirect()
            }
        }
    }

    private fun changeStatusBarColor() {
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