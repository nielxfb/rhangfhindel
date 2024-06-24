package edu.bluejack23_2.rhangfhindel.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso
import edu.bluejack23_2.rhangfhindel.BuildConfig
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.activities.LoginActivity
import edu.bluejack23_2.rhangfhindel.databinding.FragmentProfileBinding
import edu.bluejack23_2.rhangfhindel.utils.SharedPrefManager
import edu.bluejack23_2.rhangfhindel.viewmodels.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var logoutButton: MaterialButton

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        return init(inflater, container)
    }

    private fun init(inflater: LayoutInflater, container: ViewGroup?): View {
        val binding: FragmentProfileBinding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_profile, container, false
        )

        viewModel = ViewModelProvider(this)[ProfileViewModel::class.java]

        viewModel.init(requireContext())

        Picasso.get().load(
            viewModel.profileURL
        ).into(binding.profileImageView)

        binding.profileFragmentName.text = viewModel.name
        binding.profileFragmentInitial.text = viewModel.initial

        logoutButton = binding.root.findViewById(R.id.logout_button)

        setEvent()

        return binding.root
    }

    private fun setEvent() {
        logoutButton.setOnClickListener {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

}