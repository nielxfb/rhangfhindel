package edu.bluejack23_2.rhangfhindel.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.activities.RoomDetailActivity
import edu.bluejack23_2.rhangfhindel.adapters.RoomAdapter
import edu.bluejack23_2.rhangfhindel.databinding.FragmentAllRoomTransactionBinding
import edu.bluejack23_2.rhangfhindel.databinding.FragmentRangListBinding
import edu.bluejack23_2.rhangfhindel.viewmodels.RoomTransactionViewModel

class RangListFragment : Fragment() {

    private lateinit var viewModel: RoomTransactionViewModel
    private lateinit var rangListBinding: FragmentRangListBinding
    private lateinit var roomAdapter: RoomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        rangListBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_rang_list,
                container,
                false
            )
        return rangListBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[RoomTransactionViewModel::class.java]
        viewModel.onLoad(true, true, null, null, null, null, requireContext())

        initRecyclerView()
        observeViewModel()

    }

    private fun initRecyclerView() {
        val recyclerView = rangListBinding.recyclerViewAllRoomList
        roomAdapter = RoomAdapter(false, emptyList(), viewModel)
        recyclerView.adapter = roomAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        viewModel.apply {
            activeRangs.observe(viewLifecycleOwner, Observer { rooms ->
                roomAdapter.updateRooms(rooms)
            })

            bookedRoomList.observe(viewLifecycleOwner, Observer { bookedRooms ->
                if (viewModel.activeRangs.value != null) {
                    getActiveRangs()
                }
            })

            isLoading.observe(viewLifecycleOwner, Observer {
                isLoading
                rangListBinding.progressBar.visibility =
                    if (isLoading.value!!) View.VISIBLE else View.GONE
                return@Observer
            })

            redirectRoom.observe(viewLifecycleOwner, Observer { room ->
                if (room != null) {
                    val intent = Intent(requireContext(), RoomDetailActivity::class.java)
                    intent.putExtra("room", room)
                    startActivity(intent)
                }
            })
        }
    }
}