package edu.bluejack23_2.rhangfhindel.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TableLayout
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator
import com.squareup.picasso.Picasso
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.adapters.FragmentPageAdapter
import edu.bluejack23_2.rhangfhindel.databinding.FragmentProfileBinding
import edu.bluejack23_2.rhangfhindel.databinding.FragmentRoomTransactionBinding
import edu.bluejack23_2.rhangfhindel.viewmodels.ProfileViewModel
import edu.bluejack23_2.rhangfhindel.viewmodels.RoomTransactionViewModel

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [RoomTransactionFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class RoomTransactionFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    lateinit var tabLayout: TabLayout
    lateinit var viewPager2: ViewPager2
    lateinit var adapter: FragmentPageAdapter

    private lateinit var viewModel: RoomTransactionViewModel
    private lateinit var binding: FragmentRoomTransactionBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding =
            DataBindingUtil.inflate(inflater, R.layout.fragment_room_transaction, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        viewModel = ViewModelProvider(this).get(RoomTransactionViewModel::class.java)

        setupViews()
        observeViewModel()
    }

    private fun setupViews() {
        binding.apply {
            viewPager2.adapter = FragmentPageAdapter(childFragmentManager, lifecycle)

            TabLayoutMediator(tabLayout, viewPager2) { tab, position ->
                tab.text = if (position == 0) "Available Rang" else "Room Transactions"
            }.attach()

            tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
                override fun onTabSelected(tab: TabLayout.Tab?) {
                    tab?.let {
                        viewPager2.currentItem = it.position
                    }
                }

                override fun onTabUnselected(tab: TabLayout.Tab?) {}
                override fun onTabReselected(tab: TabLayout.Tab?) {}
            })
        }
    }

    private fun observeViewModel() {
        viewModel.apply {
            isLoading.observe(viewLifecycleOwner) { isLoading ->
                // Handle isLoading state (show/hide progress)
                // Example: progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            }

            errorMessage.observe(viewLifecycleOwner) { errorMessage ->
                // Handle error message
                // Example: Toast.makeText(requireContext(), errorMessage, Toast.LENGTH_SHORT).show()
            }

            success.observe(viewLifecycleOwner) { success ->
                // Handle success state if needed
            }

            roomTransactions.observe(viewLifecycleOwner) { transactions ->
                // Update UI with roomTransactions data
                // Example: adapter.submitList(transactions)
            }
        }
    }

//    fun init(inflater: LayoutInflater, container: ViewGroup?): View {
//        val binding: FragmentRoomTransactionBinding = DataBindingUtil.inflate(
//            inflater, R.layout.fragment_room_transaction, container, false
//        )
//
//        viewModel = ViewModelProvider(this).get(RoomTransactionViewModel::class.java)
//
//        tabLayout = binding.tabLayout
//        viewPager2 = binding.viewPager2
//
//        adapter = FragmentPageAdapter(childFragmentManager, lifecycle)
//
//        tabLayout.addTab(tabLayout.newTab().setText("Available Rang"))
//        tabLayout.addTab(tabLayout.newTab().setText("Room Transactions"))
//
//        viewPager2.adapter = adapter
//
//        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
//            override fun onTabSelected(tab: TabLayout.Tab?) {
//                if (tab != null) {
//                    viewPager2.post {
//                        viewPager2.currentItem = tab.position
//                    }
//                }
//            }
//
//            override fun onTabUnselected(tab: TabLayout.Tab?) {
//            }
//
//            override fun onTabReselected(tab: TabLayout.Tab?) {
//            }
//        })
//
//        return binding.root
//    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment RoomTransactionFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            RoomTransactionFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}