package edu.bluejack23_2.rhangfhindel.activities

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.widget.FrameLayout
import android.widget.LinearLayout
import android.widget.LinearLayout.LayoutParams
import android.widget.TextView
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.card.MaterialCardView
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.base.BaseActivity
import edu.bluejack23_2.rhangfhindel.databinding.ActivityRoomDetailBinding
import edu.bluejack23_2.rhangfhindel.models.Detail
import edu.bluejack23_2.rhangfhindel.models.StatusDetail
import edu.bluejack23_2.rhangfhindel.viewmodels.RoomDetailViewModel
import java.text.SimpleDateFormat
import java.util.*

class RoomDetailActivity : BaseActivity() {

    private lateinit var binding: ActivityRoomDetailBinding
    private lateinit var viewModel: RoomDetailViewModel

    var startHour = 7
    val interval = 2
    var shift = 1

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        changeStatusBarColor(R.color.transparent_background)
        init()
        setEvent()
        observeViewModel()
    }

    fun getTimeString(hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
    }

    override fun init() {
        binding = ActivityRoomDetailBinding.inflate(layoutInflater)
        setContentView(binding.root)

        viewModel = ViewModelProvider(this)[RoomDetailViewModel::class.java]

        val room: Detail? = intent.getParcelableExtra("room")

        if (room != null) {
            viewModel.room.value = room
        }

    }

    override fun setEvent() {
        binding.roomName.setOnClickListener {
            finish()
        }
    }

    private fun observeViewModel() {
        viewModel.room.observe(this) { room ->

            binding.roomName.text = "< Room ${room.RoomName}"

            binding.scheduleContainer.removeAllViews()

            val schedule = room.StatusDetails

            for (i in 1..11 step 2) {
                val endHour = startHour + interval

                val cardView = FrameLayout(this).apply {
                    layoutParams = LinearLayout.LayoutParams(
                        LinearLayout.LayoutParams.MATCH_PARENT,
                        LinearLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        setMargins(0, 20, 0, 0)
                    }
                    setPadding(20, 20, 20, 20)
                    setBackgroundResource(R.drawable.shape_white_corner_radius)

                    clipChildren = false
                    clipToPadding = false
                }

                val scheduleView = LayoutInflater.from(binding.root.context)
                    .inflate(
                        R.layout.schedule_layout,
                        cardView,
                        false
                    )

                val scheduleTextView = scheduleView.findViewById<TextView>(R.id.text_view_schedule)
                scheduleTextView.text =
                    "Shift ${shift} (${getTimeString(startHour, 0)} - ${getTimeString(endHour, 0)})"

                var backgroundColor =
                    if (schedule[i].isEmpty()) R.drawable.schedule_text_background_blue else R.drawable.schedule_text_background_red

                var textColor =
                    if (schedule[i].isEmpty()) R.color.primary_color else R.color.red

                scheduleTextView.setBackgroundResource(backgroundColor)
                scheduleTextView.setTextColor(scheduleView.context.resources.getColor(textColor))
                scheduleTextView.layoutParams.width = 400
                scheduleTextView.textSize = 12f

                startHour += interval
                shift += 1

                val flagView = TextView(this@RoomDetailActivity).apply {
                    if (schedule[i].isEmpty()) {
                        text = "Free"
                    } else {
                        if (schedule[i][0].Status == "B") {
                            text = "Borrowing"
                        } else if (schedule[i][0].Status == "L") {
                            text = "Lecture"
                        } else {
                            text = "Calibration"
                        }
                    }

                    setPadding(8, 8, 8, 8)
                    setTextColor(resources.getColor(R.color.accent))
                    setBackgroundResource(R.drawable.shape_accent_corner_radius)
                    layoutParams = FrameLayout.LayoutParams(
                        200,
                        FrameLayout.LayoutParams.WRAP_CONTENT
                    ).apply {
                        gravity = Gravity.END or Gravity.TOP
                        setMargins(60, 5, -60, 0)
                    }

                    textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                    textSize = 12f
                }

                cardView.addView(scheduleView)
                cardView.addView(flagView)
                binding.scheduleContainer.addView(cardView)
            }
        }
    }

}