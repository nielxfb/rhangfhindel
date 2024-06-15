package edu.bluejack23_2.rhangfhindel.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.Window
import android.view.WindowManager
import androidx.core.content.ContextCompat
import edu.bluejack23_2.rhangfhindel.R

abstract class BaseActivity : AppCompatActivity() {

    abstract fun init()
    abstract fun setEvent()
    fun changeStatusBarColor(id: Int) {
        val window: Window = window
        window.addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS)
        window.statusBarColor = ContextCompat.getColor(this, id)
    }

}