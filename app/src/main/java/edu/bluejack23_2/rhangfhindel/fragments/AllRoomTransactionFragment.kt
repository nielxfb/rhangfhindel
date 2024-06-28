package edu.bluejack23_2.rhangfhindel.fragments

import android.app.Dialog
import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.SearchView
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.activities.RoomDetailActivity
import edu.bluejack23_2.rhangfhindel.adapters.RoomAdapter
import edu.bluejack23_2.rhangfhindel.databinding.FilterModalBinding
import edu.bluejack23_2.rhangfhindel.databinding.FragmentAllRoomTransactionBinding
import edu.bluejack23_2.rhangfhindel.utils.Coroutines
import edu.bluejack23_2.rhangfhindel.viewmodels.RoomTransactionViewModel

class AllRoomTransactionFragment : Fragment() {

    private lateinit var viewModel: RoomTransactionViewModel
    private lateinit var allRoomTransactionBinding: FragmentAllRoomTransactionBinding
    private lateinit var roomAdapter: RoomAdapter
    private lateinit var searchBar: SearchView
    private lateinit var filterModal: Dialog
    private lateinit var filterModalBinding: FilterModalBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        allRoomTransactionBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_all_room_transaction,
                container,
                false
            )
        filterModalBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.filter_modal,
                container,
                false
            )
        return allRoomTransactionBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initFilterModal()

        viewModel = ViewModelProvider(this)[RoomTransactionViewModel::class.java]
        viewModel.onLoad(
            false,
            false,
            null,
            null,
            filterModal,
            filterModalBinding,
            requireContext()
        )

        searchBar =
            allRoomTransactionBinding.root.findViewById(R.id.search_bar_all_room_transaction)

        initRecyclerView()

        observeViewModel()

        setEvent()
    }

    private fun initFilterModal() {
        filterModal = Dialog(requireContext())
        filterModal.setContentView(filterModalBinding.root)
        filterModal.window?.setLayout(
            800,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        filterModal.setCancelable(true)
    }

    private fun initRecyclerView() {
        val recyclerView = allRoomTransactionBinding.recyclerViewAllRoomList
        roomAdapter = RoomAdapter(false, emptyList(), viewModel)
        recyclerView.adapter = roomAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun setEvent() {
        searchBar.setOnQueryTextListener(object : SearchView.OnQueryTextListener {
            override fun onQueryTextSubmit(query: String?): Boolean {
                viewModel.searchQuery.value = query
                return true
            }

            override fun onQueryTextChange(newText: String?): Boolean {
                viewModel.searchQuery.value = newText
                return true
            }
        })
        allRoomTransactionBinding.filterButtonRooms.setOnClickListener {
            viewModel.showFilterModal()
        }
        filterModalBinding.applyFilterButton.setOnClickListener {
            val is6Floor = filterModalBinding.checkbox6Floor.isChecked
            val is7Floor = filterModalBinding.checkbox7Floor.isChecked
            viewModel.onApplyFilterRoom(is6Floor, is7Floor)
        }
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

            bookedRoomList.observe(requireActivity(), Observer { bookedRooms ->
                if (roomTransactions.value != null) {
                    roomAdapter.updateRooms(roomTransactions.value!!)
                }
            })

            searchQuery.observe(viewLifecycleOwner, Observer { query ->
                roomTransactions.value = allRooms
                roomTransactions.value = roomTransactions.value!!.filter { room ->
                    room.RoomName.contains(query)
                }
            })
        }
    }
}