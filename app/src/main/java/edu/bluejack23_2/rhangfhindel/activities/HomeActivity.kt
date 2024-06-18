package edu.bluejack23_2.rhangfhindel.activities

import android.os.Bundle
import androidx.navigation.NavController
import androidx.navigation.findNavController
import androidx.navigation.ui.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.base.BaseActivity

class HomeActivity : BaseActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)
        changeStatusBarColor(R.color.transparent_background)
        init()
    }

    override fun init() {
        val navController = findNavController(R.id.curr_fragment)
        val navbarView = findViewById<BottomNavigationView>(R.id.bottom_navigation)
        navbarView.setupWithNavController(navController)
    }

    override fun setEvent() {
        TODO("Not yet implemented")
    }
}