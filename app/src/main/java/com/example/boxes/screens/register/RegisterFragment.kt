package com.example.boxes.screens.register

import android.app.Activity
import android.content.Intent
import android.graphics.drawable.BitmapDrawable
import android.net.Uri
import android.os.Bundle
import android.provider.MediaStore
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
import com.example.boxes.databinding.FragmentRegisterBinding


class RegisterFragment : Fragment() {

    private lateinit var viewModel: RegisterViewModel

    private lateinit var binding: FragmentRegisterBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_register, container, false)

        viewModel = ViewModelProviders.of(this).get(RegisterViewModel::class.java)
        binding.viewModel = viewModel
        binding.lifecycleOwner = this

        viewModel.toLogin.observe(this, Observer {state ->
            navigateToLoginFragment(state)
        })

        viewModel.goSelectPhoto.observe(this, Observer {state ->
            selectPhoto(state)
        })

        viewModel.getPassAndEmail.observe(this, Observer {state ->
            if(state) {
                val email = binding.emailEdittextRegistration.text.toString()
                val password = binding.passwordEdittextRegistration.text.toString()
                val username = binding.usernameEdittextRegistration.text.toString()
                if (email.isEmpty() || password.isEmpty() || username.isEmpty()) {
                    Toast.makeText(activity, "Please enter username, email and password", Toast.LENGTH_SHORT)
                        .show()
                }else
                viewModel.register(email, password, username)
            }
        })

        viewModel.showFailureToast.observe(this, Observer { message ->
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        })

        viewModel.toInbox.observe(this, Observer {state ->
            navigateToInboxFragment(state)
        })

        return binding.root
    }

     private fun navigateToLoginFragment(_state: Boolean) {
        if(_state) {
            findNavController().navigate(R.id.action_registerFragment_to_loginFragment)
            viewModel.endNavigateToLoginFragment()
        }
    }

    private fun navigateToInboxFragment(_state: Boolean) {
        if(_state) {
            findNavController().navigate(R.id.action_registerFragment_to_inboxFragment)
            viewModel.endNavigateToInboxFragment()
        }
    }

    fun selectPhoto(_state: Boolean){
        if(_state) {
            val intent = Intent(Intent.ACTION_PICK)
            intent.type = "image/*"
            startActivityForResult(intent, 0)
            viewModel.endSelectPhoto()
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if(requestCode==0 && resultCode == Activity.RESULT_OK && data!=null){
            val uri = data.data
            val bitmap = MediaStore.Images.Media.getBitmap(activity?.contentResolver, uri)
            binding.selectphotoImageviewRegister.setImageBitmap(bitmap)
            binding.selectphotoButtonRegister.alpha = 0f
            viewModel.selectedPhotoUri = uri
        }
    }

}
