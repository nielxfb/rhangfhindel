package edu.bluejack23_2.rhangfhindel.activities

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.base.BaseActivity

class DashboardActivity : BaseActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        changeStatusBarColor(R.color.light_highlight)
    }
    override fun init() {
        TODO("Not yet implemented")
    }

    override fun setEvent() {
        TODO("Not yet implemented")
    }
}