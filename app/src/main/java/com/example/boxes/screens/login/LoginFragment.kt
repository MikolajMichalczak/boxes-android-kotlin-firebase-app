package com.example.boxes.screens.login

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController

import com.example.boxes.R
import com.example.boxes.databinding.FragmentLoginBinding


class LoginFragment : Fragment() {

    private lateinit var viewModel: LoginViewModel

    private lateinit var binding: FragmentLoginBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_login, container, false)

        viewModel = ViewModelProviders.of(this).get(LoginViewModel::class.java)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        viewModel.toRegister.observe(this, Observer {state ->
            navigateToRegisterFragment(state)
        })

        viewModel.toInbox.observe(this, Observer {state ->
            navigateToInboxFragment(state)
        })

        viewModel.getPassAndEmail.observe(this, Observer {state ->
            if(state) {
                val email = binding.emailEdittextLogin.text.toString()
                val password = binding.passwordEdittextLogin.text.toString()
                if (email.isEmpty() || password.isEmpty()) {
                    Toast.makeText(activity, "Please enter email and password", Toast.LENGTH_SHORT)
                        .show()
                }else
                    viewModel.login(email, password)
            }
        })
        return binding.root
    }

    private fun navigateToRegisterFragment(_state: Boolean) {
        if(_state) {
            findNavController().navigate(R.id.action_loginFragment_to_registerFragment)
            viewModel.endNavigateToRegisterFragment()
        }
    }

    private fun navigateToInboxFragment(_state: Boolean) {
        if(_state) {
            findNavController().navigate(R.id.action_loginFragment_to_inboxFragment)
            viewModel.endNavigateToInboxFragment()
        }
    }

}
