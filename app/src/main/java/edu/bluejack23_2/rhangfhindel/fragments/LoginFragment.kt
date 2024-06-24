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
import edu.bluejack23_2.rhangfhindel.R
import edu.bluejack23_2.rhangfhindel.activities.HomeActivity
import edu.bluejack23_2.rhangfhindel.databinding.FragmentLoginBinding
import edu.bluejack23_2.rhangfhindel.factories.LoginViewModelFactory
import edu.bluejack23_2.rhangfhindel.utils.PopUp
import edu.bluejack23_2.rhangfhindel.viewmodels.LoginViewModel

class LoginFragment : Fragment() {

    private var _binding: FragmentLoginBinding? = null
    private val binding get() = _binding!!

    private lateinit var viewModel: LoginViewModel

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val factory = LoginViewModelFactory(requireActivity())
        viewModel = ViewModelProvider(this, factory)[LoginViewModel::class.java]

        _binding = FragmentLoginBinding.inflate(inflater, container, false)
        binding.viewmodel = viewModel
        binding.lifecycleOwner = viewLifecycleOwner

        setEvent()

        return binding.root
    }

    private fun setEvent() {
        binding.loginButton.setOnClickListener {
            viewModel.onLoginButtonClick()
        }

        viewModel.success.observe(requireActivity(), Observer { success ->
            if (!success) {
                PopUp.shortDuration(requireActivity(), "Credentials invalid!")
                return@Observer
            }

            PopUp.shortDuration(requireActivity(), "Login success!")
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
        val intent = Intent(requireActivity(), HomeActivity::class.java)
        startActivity(intent)
        requireActivity().finish()
    }
}