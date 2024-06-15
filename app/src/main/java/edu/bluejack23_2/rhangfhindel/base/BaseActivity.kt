package edu.bluejack23_2.rhangfhindel.base

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle

abstract class BaseActivity : AppCompatActivity() {

    abstract fun init()
    abstract fun setEvent()
    abstract fun changeStatusBarColor()

}