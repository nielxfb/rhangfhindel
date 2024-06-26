package edu.bluejack23_2.rhangfhindel.fragments

import android.content.Intent
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageButton
import android.widget.LinearLayout
import androidx.biometric.BiometricManager
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProvider
import com.google.android.material.button.MaterialButton
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.activities.HomeActivity
import edu.bluejack23_2.rhangfhindel.activities.LoginActivity
import edu.bluejack23_2.rhangfhindel.databinding.FragmentLoggedInBinding
import edu.bluejack23_2.rhangfhindel.factories.LoginViewModelFactory
import edu.bluejack23_2.rhangfhindel.utils.PopUp
import edu.bluejack23_2.rhangfhindel.utils.SharedPrefManager
import edu.bluejack23_2.rhangfhindel.viewmodels.LoginViewModel

class LoggedInFragment : Fragment() {

    private lateinit var loginAnotherAccountButton: MaterialButton
    private lateinit var loginButton: MaterialButton

    private var _binding: FragmentLoggedInBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val factory = LoginViewModelFactory(requireActivity())
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        _binding = FragmentLoggedInBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        init(binding)
        setEvent()

        val biometricManager = BiometricManager.from(requireContext())
        when (biometricManager.canAuthenticate(BiometricManager.Authenticators.BIOMETRIC_STRONG or BiometricManager.Authenticators.DEVICE_CREDENTIAL)) {
            BiometricManager.BIOMETRIC_SUCCESS or BiometricManager.BIOMETRIC_ERROR_NONE_ENROLLED ->
                binding.root.findViewById<ImageButton>(R.id.login_biometrics_button).visibility =
                    View.VISIBLE

            else -> {
                binding.root.findViewById<ImageButton>(R.id.login_biometrics_button).visibility =
                    View.GONE
                val params = loginButton.layoutParams as LinearLayout.LayoutParams
                params.marginEnd = 0
            }
        }

        return binding.root
    }

    private fun init(binding: FragmentLoggedInBinding) {
        loginButton = binding.root.findViewById(R.id.login_button)
        loginAnotherAccountButton = binding.root.findViewById(R.id.login_another_account_button)
    }

    private fun setEvent() {
        loginAnotherAccountButton.setOnClickListener {
            SharedPrefManager.clearAssistant(requireActivity())
            startActivity(Intent(requireActivity(), LoginActivity::class.java))
            requireActivity().finish()
        }

        loginButton.setOnClickListener {
            viewModel.username = viewModel.assistantUsername.value!!
            viewModel.onLoginButtonClick()
        }

        viewModel.success.observe(requireActivity(), Observer { success ->
            if (!success) {
                PopUp.shortDuration(requireActivity(), getString(R.string.credentials_invalid))
                return@Observer
            }

            PopUp.shortDuration(requireActivity(), getString(R.string.login_success))
            redirect()
        })

        viewModel.errorMessage.observe(requireActivity(), Observer { message ->
            if (message.isNotEmpty()) {
                PopUp.shortDuration(requireActivity(), message)
                return@Observer
            }
        })

        viewModel.isLoading.observe(requireActivity(), Observer { isLoading ->
            binding.progressBar.visibility = if (isLoading) View.VISIBLE else View.GONE
            return@Observer
        })
    }

    private fun redirect() {
        startActivity(Intent(requireActivity(), HomeActivity::class.java))
        requireActivity().finish()
    }

}