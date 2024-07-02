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
import androidx.navigation.NavOptions
import androidx.navigation.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.activities.RoomDetailActivity
import edu.bluejack23_2.rhangfhindel.adapters.DashboardRoomAdapter
import edu.bluejack23_2.rhangfhindel.databinding.FragmentDashboardBinding
import edu.bluejack23_2.rhangfhindel.viewmodels.HomeViewModel

class DashboardFragment : Fragment() {

    private lateinit var viewModel: HomeViewModel
    private lateinit var dashboardBinding: FragmentDashboardBinding
    private lateinit var roomAdapter: DashboardRoomAdapter
    private lateinit var activeRangAdapter: DashboardRoomAdapter

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        dashboardBinding =
            DataBindingUtil.inflate(
                inflater,
                R.layout.fragment_dashboard,
                container,
                false
            )
        return dashboardBinding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this)[HomeViewModel::class.java]
        viewModel.onLoad(requireContext())
        dashboardBinding.textViewGreeting.text = viewModel.assistant.value?.Username

        initRecyclerView()

        observeViewModel()

        setEvent()
    }

    private fun initRecyclerView() {
        val recyclerView = dashboardBinding.recyclerViewDashboardAvailableRang
        roomAdapter = DashboardRoomAdapter(false, emptyList(), viewModel)
        recyclerView.adapter = roomAdapter
        recyclerView.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)

        val recyclerViewActiveRang = dashboardBinding.recyclerViewDashboardAvailableActiveRang
        activeRangAdapter = DashboardRoomAdapter(true, emptyList(), viewModel)
        recyclerViewActiveRang.adapter = activeRangAdapter
        recyclerViewActiveRang.layoutManager =
            LinearLayoutManager(requireContext(), LinearLayoutManager.HORIZONTAL, false)
    }

    private fun observeViewModel() {
        viewModel.apply {
            rangs.observe(viewLifecycleOwner, Observer { rang ->
                if (rang.isNotEmpty()) {
                    roomAdapter.updateRooms(rang)
                    dashboardBinding.recyclerViewDashboardAvailableRang.visibility = View.VISIBLE
                    dashboardBinding.noRoomsFoundAvailRang.visibility = View.GONE
                } else {
                    dashboardBinding.recyclerViewDashboardAvailableRang.visibility = View.GONE
                    dashboardBinding.noRoomsFoundAvailRang.visibility = View.VISIBLE
                }
            })

            activeRangs.observe(viewLifecycleOwner, Observer { rooms ->
                if (rooms.isNotEmpty()) {
                    activeRangAdapter.updateRooms(rooms)
                    dashboardBinding.recyclerViewDashboardAvailableActiveRang.visibility =
                        View.VISIBLE
                    dashboardBinding.noRoomsFoundBooked.visibility = View.GONE
                } else {
                    dashboardBinding.recyclerViewDashboardAvailableActiveRang.visibility = View.GONE
                    dashboardBinding.noRoomsFoundBooked.visibility = View.VISIBLE
                }
            })

            isLoading.observe(viewLifecycleOwner, Observer {
                isLoading
                dashboardBinding.progressBar.visibility =
                    if (isLoading.value!!) View.VISIBLE else View.GONE
                return@Observer
            })

            bookedRoomList.observe(requireActivity(), Observer { bookedRooms ->
                if (rangs.value != null) {
                    roomAdapter.updateRooms(rangs.value!!)
                }
                if (viewModel.activeRangs.value != null) {
                    getActiveRangs()
                }
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

    private fun setEvent() {
        dashboardBinding.viewAvailableRangButton.setOnClickListener {
            val action =
                DashboardFragmentDirections.actionDashboardFragmentToRoomTransactionFragment()
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.dashboard_fragment, true)
                .build()
            it.findNavController().navigate(action, navOptions)
        }

        dashboardBinding.viewBookedRangButton.setOnClickListener {
            val action =
                DashboardFragmentDirections.actionDashboardFragmentToRangListFragment()
            val navOptions = NavOptions.Builder()
                .setPopUpTo(R.id.dashboard_fragment, true)
                .build()
            it.findNavController().navigate(action, navOptions)
        }
    }
}