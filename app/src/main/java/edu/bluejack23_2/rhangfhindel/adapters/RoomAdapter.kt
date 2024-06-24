package edu.bluejack23_2.rhangfhindel.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import android.widget.GridLayout
import android.widget.TextView
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.databinding.RecyclerViewRoomBinding
import edu.bluejack23_2.rhangfhindel.models.Detail
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale

class RoomAdapter(
    private var rooms: List<Detail>
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
        holder.bind(room)
        populateSchedule(holder.binding)
    }

    fun getTimeString(hour: Int, minute: Int): String {
        val calendar = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, hour)
            set(Calendar.MINUTE, minute)
        }
        return SimpleDateFormat("HH:mm", Locale.getDefault()).format(calendar.time)
    }

    private fun populateSchedule(binding: RecyclerViewRoomBinding) {
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

            val params = GridLayout.LayoutParams().apply {
                width = GridLayout.LayoutParams.WRAP_CONTENT
                height = GridLayout.LayoutParams.WRAP_CONTENT
                rowSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                columnSpec = GridLayout.spec(GridLayout.UNDEFINED, 1f)
                setMargins(2, 2, 2, 2)
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
