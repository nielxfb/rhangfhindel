package edu.bluejack23_2.rhangfhindel.adapters

import android.app.Dialog
import android.util.Log
import android.view.Gravity
import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.FrameLayout
import android.widget.GridLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.databinding.BookModalBinding
import edu.bluejack23_2.rhangfhindel.databinding.RecyclerViewRoomBinding
import edu.bluejack23_2.rhangfhindel.models.Detail
import edu.bluejack23_2.rhangfhindel.viewmodels.RoomTransactionViewModel
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RoomAdapter(
    private var rang: Boolean,
    private var rooms: List<Detail>,
    private val viewModel: RoomTransactionViewModel,
) : RecyclerView.Adapter<RoomAdapter.RoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val binding = DataBindingUtil.inflate<RecyclerViewRoomBinding>(
            LayoutInflater.from(parent.context),
            R.layout.recycler_view_room,
            parent,
            false
        )
        return RoomViewHolder(binding)
    }

    override fun getItemCount() = rooms.size

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        var initial = ""

        holder.bind(room)
        if (viewModel.bookedRoomList.value!!.contains(room.RoomName)) {
            initial =
                viewModel.bookedInitialList.value!![viewModel.bookedRoomList.value!!.indexOf(room.RoomName)]

            val flagView = TextView(viewModel.context).apply {
                text = "Booked by $initial"

                Log.d("TESTES", "MASUK")

                setPadding(8, 8, 8, 8)
                setTextColor(resources.getColor(R.color.accent))
                setBackgroundResource(R.drawable.shape_accent_corner_radius)
                layoutParams = FrameLayout.LayoutParams(
                    400,
                    FrameLayout.LayoutParams.WRAP_CONTENT
                ).apply {
                    gravity = Gravity.END or Gravity.TOP
                    setMargins(0, 5, 0, 0)
                }

                textAlignment = TextView.TEXT_ALIGNMENT_CENTER
                textSize = 12f
            }

            holder.binding.cardView.addView(flagView)

            holder.binding.root.setOnClickListener {
                if (!rang) {
                    viewModel.redirectRoom.value = room
                } else {
                    viewModel.message.value = "Room already booked"
                }
            }

            return;

        }

        if (rang) {
            holder.binding.root.setOnClickListener {
                viewModel.showRangModal(room.RoomName)
                viewModel.currRoom = room
            }
        } else {
            holder.binding.root.setOnClickListener {
                viewModel.redirectRoom.value = room
            }
        }
        populateSchedule(holder.binding, room)
    }


    fun getTimeString(hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
    }

    private fun populateSchedule(binding: RecyclerViewRoomBinding, room: Detail) {
        val statusDetails = room.StatusDetails

        val gridLayout = binding.gridLayoutSchedule
        gridLayout.removeAllViews()

        var startHour = 7
        val interval = 2
        var shift = 1

        for (i in 1..11 step 2) {
            val endHour = startHour + interval

            val scheduleView = LayoutInflater.from(binding.root.context)
                .inflate(
                    R.layout.schedule_layout,
                    gridLayout,
                    false
                )

            val scheduleTextView = scheduleView.findViewById<TextView>(R.id.text_view_schedule)
            scheduleTextView.text =
                "Shift ${shift} (${getTimeString(startHour, 0)} - ${getTimeString(endHour, 0)})"

            var backgroundColor =
                if (statusDetails[i].isEmpty()) R.drawable.schedule_text_background_blue else R.drawable.schedule_text_background_red

            var textColor =
                if (statusDetails[i].isEmpty()) R.color.primary_color else R.color.red

            scheduleTextView.setBackgroundResource(backgroundColor)
            scheduleTextView.setTextColor(scheduleView.context.resources.getColor(textColor))

            val params = GridLayout.LayoutParams().apply {
                width = GridLayout.LayoutParams.WRAP_CONTENT
                height = GridLayout.LayoutParams.WRAP_CONTENT
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(5, 5, 5, 5)
            }

            gridLayout.addView(scheduleView, params)
            startHour += interval
            shift += 1
        }
    }

    fun updateRooms(newRooms: List<Detail>) {
        rooms = newRooms
        notifyDataSetChanged()
    }

    inner class RoomViewHolder(
        val binding: RecyclerViewRoomBinding
    ) : RecyclerView.ViewHolder(binding.root) {

        fun bind(room: Detail) {
            binding.room = room
            binding.executePendingBindings()
        }
    }
}
