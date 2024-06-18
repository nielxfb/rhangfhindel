package edu.bluejack23_2.rhangfhindel.activities

import android.os.Bundle
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.base.BaseActivity

class HomeActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_home)

        changeStatusBarColor(R.color.light_border_color)
    }

    override fun init() {
        TODO("Not yet implemented")
    }

    override fun setEvent() {
        TODO("Not yet implemented")
    }
}