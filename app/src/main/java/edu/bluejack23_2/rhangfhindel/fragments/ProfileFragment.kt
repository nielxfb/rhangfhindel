package edu.bluejack23_2.rhangfhindel.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.biometric.BiometricPrompt
import androidx.core.content.ContextCompat
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import com.squareup.picasso.Picasso
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.activities.LoginActivity
import edu.bluejack23_2.rhangfhindel.databinding.FragmentProfileBinding
import edu.bluejack23_2.rhangfhindel.utils.PopUp
import edu.bluejack23_2.rhangfhindel.utils.SharedPrefManager
import edu.bluejack23_2.rhangfhindel.viewmodels.ProfileViewModel

class ProfileFragment : Fragment() {

    private lateinit var viewModel: ProfileViewModel
    private lateinit var logoutButton: MaterialButton
    private lateinit var biometricPrompt: BiometricPrompt
    private lateinit var promptInfo: BiometricPrompt.PromptInfo

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

        biometricPrompt = BiometricPrompt(this, ContextCompat.getMainExecutor(requireContext()),
            object : BiometricPrompt.AuthenticationCallback() {

                override fun onAuthenticationError(errorCode: Int, errString: CharSequence) {
                    super.onAuthenticationError(errorCode, errString)
                    PopUp.shortDuration(requireContext(), "Authentication error: $errString")
                }

                override fun onAuthenticationSucceeded(result: BiometricPrompt.AuthenticationResult) {
                    super.onAuthenticationSucceeded(result)
                    val ast = SharedPrefManager.getAssistant(requireContext())
                    ast!!.UseBiometric = true
                    SharedPrefManager.saveAssistant(requireContext(), ast)
                    PopUp.shortDuration(
                        requireContext(),
                        getString(R.string.biometrics_setup_success)
                    )
                }

                override fun onAuthenticationFailed() {
                    super.onAuthenticationFailed()
                    PopUp.shortDuration(requireContext(), getString(R.string.credentials_invalid))
                }

            })

        // TODO: Set localization
        promptInfo = BiometricPrompt.PromptInfo.Builder()
            .setTitle("Login with Biometric")
            .setSubtitle("Please use your biometric credentials to continue")
            .setNegativeButtonText("Use password")
            .build()

        setEvent()

        binding.root.findViewById<MaterialButton>(R.id.biometric_setup_button).setOnClickListener {
            biometricPrompt.authenticate(promptInfo)
        }

        return binding.root
    }

    private fun setEvent() {
        logoutButton.setOnClickListener {
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }
    }

}