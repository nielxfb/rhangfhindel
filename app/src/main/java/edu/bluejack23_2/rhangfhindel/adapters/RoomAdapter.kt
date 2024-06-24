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

    private fun populateSchedule(binding: RecyclerViewRoomBinding) {
        val gridLayout = binding.gridLayoutSchedule
        gridLayout.removeAllViews()

        for (i in 1..11 step 2) {
            val scheduleView = LayoutInflater.from(binding.root.context)
                .inflate(
                    R.layout.schedule_layout,
                    gridLayout,
                    false
                )

            val scheduleTextView = scheduleView.findViewById<TextView>(R.id.text_view_schedule)
            scheduleTextView.text = "TESTES"

            val params = GridLayout.LayoutParams().apply {
                width = GridLayout.LayoutParams.WRAP_CONTENT
                height = GridLayout.LayoutParams.WRAP_CONTENT
            }

            gridLayout.addView(scheduleView, params)
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
