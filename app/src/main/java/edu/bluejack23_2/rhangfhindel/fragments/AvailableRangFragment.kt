package edu.bluejack23_2.rhangfhindel.fragments

import android.app.Dialog
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
import edu.bluejack23_2.rhangfhindel.adapters.RoomAdapter
import edu.bluejack23_2.rhangfhindel.databinding.BookModalBinding
import edu.bluejack23_2.rhangfhindel.databinding.FragmentAvailableRangBinding
import edu.bluejack23_2.rhangfhindel.databinding.RecyclerViewRoomBinding
import edu.bluejack23_2.rhangfhindel.databinding.ScheduleLayoutBinding
import edu.bluejack23_2.rhangfhindel.utils.PopUp
import edu.bluejack23_2.rhangfhindel.viewmodels.RoomTransactionViewModel

class AvailableRangFragment : Fragment() {

    private lateinit var viewModel: RoomTransactionViewModel
    private lateinit var availableRangBinding: FragmentAvailableRangBinding
    private lateinit var recyclerViewRoomBinding: RecyclerViewRoomBinding
    private lateinit var scheduleLayoutBinding: ScheduleLayoutBinding
    private lateinit var modalBinding: BookModalBinding
    private lateinit var roomAdapter: RoomAdapter

    private lateinit var modal: Dialog
    private lateinit var searchBar: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        availableRangBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_available_rang, container, false)
        modalBinding = DataBindingUtil.inflate(inflater, R.layout.book_modal, container, false)
        return availableRangBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initModal()

        viewModel = ViewModelProvider(this)[RoomTransactionViewModel::class.java]
        viewModel.onLoad(true, true, modal, modalBinding, requireContext())

        searchBar = availableRangBinding.root.findViewById(R.id.search_bar_rang_list)

        initRecyclerView()

        observeViewModel()

        setEvent()
    }

    private fun setEvent() {
        modalBinding.buttonCancel.setOnClickListener {
            viewModel.closeModal()
        }
        modalBinding.buttonYes.setOnClickListener {
            viewModel.bookRang(requireContext())
        }
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
    }

    private fun initModal() {
        modal = Dialog(requireContext())
        modal.setContentView(modalBinding.root)
        modal.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        modal.setCancelable(true)
    }

    private fun initRecyclerView() {
        val recyclerView = availableRangBinding.recyclerViewRangList
        roomAdapter = RoomAdapter(true, emptyList(), viewModel)
        recyclerView.adapter = roomAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        viewModel.apply {
            rangs.observe(viewLifecycleOwner, Observer { rooms ->
                roomAdapter.updateRooms(rooms)
                return@Observer
            })

            isLoading.observe(viewLifecycleOwner, Observer {
                isLoading
                availableRangBinding.progressBar.visibility =
                    if (isLoading.value!!) View.VISIBLE else View.GONE
                return@Observer
            })

            message.observe(viewLifecycleOwner, Observer { message ->
                if (message.isNotEmpty()) {
                    PopUp.shortDuration(requireActivity(), message)

                    viewModel.message.value = ""
                    return@Observer
                }
            })

            bookedRoomList.observe(viewLifecycleOwner, Observer { bookedRooms ->
                if (rangs.value != null) {
                    roomAdapter.updateRooms(rangs.value!!)
                }
                return@Observer
            })
            searchQuery.observe(viewLifecycleOwner, Observer { query ->
                rangs.value = allRangs
                rangs.value = rangs.value!!.filter { room ->
                    room.RoomName.contains(query)
                }
            })
        }
    }
}