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
import edu.bluejack23_2.rhangfhindel.databinding.FilterModalBinding
import edu.bluejack23_2.rhangfhindel.databinding.FragmentAvailableRangBinding
import edu.bluejack23_2.rhangfhindel.databinding.RecyclerViewRoomBinding
import edu.bluejack23_2.rhangfhindel.databinding.ScheduleLayoutBinding
import edu.bluejack23_2.rhangfhindel.utils.PopUp
import edu.bluejack23_2.rhangfhindel.viewmodels.RoomTransactionViewModel

class AvailableRangFragment : Fragment() {

    private lateinit var viewModel: RoomTransactionViewModel
    private lateinit var availableRangBinding: FragmentAvailableRangBinding
    private lateinit var alternativeAdapter: RoomAdapter
    private lateinit var bookModalBinding: BookModalBinding
    private lateinit var filterModalBinding: FilterModalBinding
    private lateinit var roomAdapter: RoomAdapter

    private lateinit var bookModal: Dialog
    private lateinit var filterModal: Dialog
    private lateinit var searchBar: SearchView

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        availableRangBinding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_available_rang, container, false)
        bookModalBinding =
            DataBindingUtil.inflate(inflater, R.layout.book_modal, container, false)
        filterModalBinding =
            DataBindingUtil.inflate(inflater, R.layout.filter_modal, container, false)
        return availableRangBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        initBookModal()
        initFilterModal()

        viewModel = ViewModelProvider(this)[RoomTransactionViewModel::class.java]
        viewModel.onLoad(
            fetchRang = true,
            fetchAlternatives = true,
            bookModal,
            bookModalBinding,
            filterModal,
            filterModalBinding,
            requireContext()
        )

        searchBar = availableRangBinding.root.findViewById(R.id.search_bar_rang_list)

        initRecyclerView()

        observeViewModel()

        setEvent()
    }

    private fun setEvent() {
        bookModalBinding.buttonCancel.setOnClickListener {
            viewModel.closeBookModal()
        }
        bookModalBinding.buttonYes.setOnClickListener {
            viewModel.bookRang(requireContext())
        }
        filterModalBinding.applyFilterButton.setOnClickListener {
            val is6Floor = filterModalBinding.checkbox6Floor.isChecked
            val is7Floor = filterModalBinding.checkbox7Floor.isChecked
            viewModel.onApplyFilterRang(is6Floor, is7Floor)
        }
        availableRangBinding.filterButton.setOnClickListener {
            viewModel.showFilterModal()
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

    private fun initBookModal() {
        bookModal = Dialog(requireContext())
        bookModal.setContentView(bookModalBinding.root)
        bookModal.window?.setLayout(
            ViewGroup.LayoutParams.WRAP_CONTENT,
            ViewGroup.LayoutParams.WRAP_CONTENT
        )
        bookModal.setCancelable(true)
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
        val recyclerView = availableRangBinding.recyclerViewRangList
        roomAdapter = RoomAdapter(true, emptyList(), viewModel)
        recyclerView.adapter = roomAdapter
        recyclerView.layoutManager = LinearLayoutManager(requireContext())

        val alternativeRecyclerView = availableRangBinding.recyclerViewAlternativeRang
        alternativeAdapter = RoomAdapter(true, emptyList(), viewModel)
        alternativeRecyclerView.adapter = alternativeAdapter
        alternativeRecyclerView.layoutManager = LinearLayoutManager(requireContext())
    }

    private fun observeViewModel() {
        viewModel.apply {
            rangs.observe(viewLifecycleOwner, Observer { rooms ->
                roomAdapter.updateRooms(rooms)
                return@Observer
            })

            alternatives.observe(viewLifecycleOwner, Observer { alternatives ->
                alternativeAdapter.updateRooms(alternatives)
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
                    alternativeAdapter.updateRooms(alternatives.value!!)
                }
                return@Observer
            })
            
            searchQuery.observe(viewLifecycleOwner, Observer { query ->
                rangs.value = allRangs
                rangs.value = rangs.value!!.filter { room ->
                    room.RoomName.contains(query)
                }

                alternatives.value = allAlternatives
                alternatives.value = alternatives.value!!.filter { room ->
                    room.RoomName.contains(query)
                }
            })
        }
    }
}