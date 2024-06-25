package edu.bluejack23_2.rhangfhindel.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.GridLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.adapters.RoomAdapter
import edu.bluejack23_2.rhangfhindel.databinding.FragmentAvailableRangBinding
import edu.bluejack23_2.rhangfhindel.databinding.RecyclerViewRoomBinding
import edu.bluejack23_2.rhangfhindel.databinding.ScheduleLayoutBinding
import edu.bluejack23_2.rhangfhindel.models.Detail
import edu.bluejack23_2.rhangfhindel.viewmodels.RoomTransactionViewModel

class AvailableRangFragment : Fragment() {

    private lateinit var viewModel: RoomTransactionViewModel
    private lateinit var availableRangBinding: FragmentAvailableRangBinding
    private lateinit var recyclerViewRoomBinding: RecyclerViewRoomBinding
    private lateinit var scheduleLayoutBinding: ScheduleLayoutBinding
    private lateinit var roomAdapter: RoomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        availableRangBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_available_rang, container, false)
        return availableRangBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[RoomTransactionViewModel::class.java]
        viewModel.onLoad(true, true)

        initRecyclerView()
        observeViewModel()
    }

    private fun initRecyclerView() {
        val recyclerView = availableRangBinding.recyclerViewRangList
        roomAdapter = RoomAdapter(emptyList()) // Initialize with an empty list
        recyclerView.adapter = roomAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        viewModel.apply {
            roomTransactions.observe(viewLifecycleOwner, Observer { rooms ->
                roomAdapter.updateRooms(rooms)
            })
        }
    }
}