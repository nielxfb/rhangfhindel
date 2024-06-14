package edu.bluejack23_2.rhangfhindel.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.bluejack23_2.rhangfhindel.R
import android.os.Build
import android.view.Window
import android.view.WindowManager

class LoginActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)

        changeStatusBarColor()
    }

    private fun changeStatusBarColor() {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = this.resources.getColor(R.color.primary_color)
    }
}