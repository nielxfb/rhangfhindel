package edu.bluejack23_2.rhangfhindel.adapters

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import edu.bluejack23_2.rhangfhindel.fragments.AllRoomTransactionFragment
import edu.bluejack23_2.rhangfhindel.fragments.AvailableRangFragment

class FragmentPageAdapter(
    fragmentManager: FragmentManager,
    lifecycle: Lifecycle
) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            AvailableRangFragment()
        } else {
            AllRoomTransactionFragment()
        }
    }

}