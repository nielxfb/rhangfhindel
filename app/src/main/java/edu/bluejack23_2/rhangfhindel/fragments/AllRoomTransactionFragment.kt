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
import edu.bluejack23_2.rhangfhindel.viewmodels.RoomTransactionViewModel

class AllRoomTransactionFragment : Fragment() {

    private lateinit var viewModel: RoomTransactionViewModel
    private lateinit var allRoomTransactionBinding: FragmentAllRoomTransactionBinding
    private lateinit var roomAdapter: RoomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        allRoomTransactionBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_all_room_transaction,
                container,
                false
            )
        return allRoomTransactionBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[RoomTransactionViewModel::class.java]
        viewModel.onLoad(false, false, null, null)

        initRecyclerView()

        observeViewModel()
    }

    private fun initRecyclerView() {
        val recyclerView = allRoomTransactionBinding.recyclerViewAllRoomList
        roomAdapter = RoomAdapter(false, emptyList(), viewModel)
        recyclerView.adapter = roomAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        viewModel.apply {
            roomTransactions.observe(viewLifecycleOwner, Observer { rooms ->
                roomAdapter.updateRooms(rooms)
            })

            redirectRoom.observe(viewLifecycleOwner, Observer { room ->
                if (room != null) {
                    val intent = Intent(requireContext(), RoomDetailActivity::class.java)
                    intent.putExtra("room", room)
                    startActivity(intent)
                }
            })

            isLoading.observe(viewLifecycleOwner, Observer {
                isLoading
                allRoomTransactionBinding.progressBar.visibility =
                    if (isLoading.value!!) View.VISIBLE else View.GONE
                return@Observer
            })
        }
    }
}