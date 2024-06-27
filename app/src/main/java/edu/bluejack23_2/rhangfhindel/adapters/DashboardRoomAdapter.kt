package edu.bluejack23_2.rhangfhindel.adapters

import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.recyclerview.widget.RecyclerView
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.databinding.DashboardRecyclerViewItemBinding
import edu.bluejack23_2.rhangfhindel.models.Detail
import edu.bluejack23_2.rhangfhindel.viewmodels.HomeViewModel

class DashboardRoomAdapter(
    private var activeRang: Boolean,
    private var rooms: List<Detail>,
    private val viewModel: HomeViewModel,
) : RecyclerView.Adapter<DashboardRoomAdapter.RoomViewHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RoomViewHolder {
        val binding = DataBindingUtil.inflate<DashboardRecyclerViewItemBinding>(
            LayoutInflater.from(parent.context),
            R.layout.dashboard_recycler_view_item,
            parent,
            false
        )
        return RoomViewHolder(binding)
    }

    override fun getItemCount() = rooms.size

    override fun onBindViewHolder(holder: RoomViewHolder, position: Int) {
        val room = rooms[position]
        holder.bind(room)

        if (!activeRang) {
            holder.binding.root.setOnClickListener {
                viewModel.redirectRoom.value = room
            }
        } else {
            holder.binding.root.setBackgroundResource(R.drawable.shape_accent_corner_radius_border)
            holder.binding.textViewRoomNumber.apply {
                setTextColor(resources.getColor(R.color.accent))
            }
        }
    }

    fun updateRooms(newRooms: List<Detail>) {
        rooms = newRooms
        notifyDataSetChanged()
    }

    inner class RoomViewHolder(
        val binding: DashboardRecyclerViewItemBinding
    ) : RecyclerView.ViewHolder(binding.root) {
        fun bind(room: Detail) {
            binding.room = room
            binding.executePendingBindings()
        }
    }
}