package com.example.boxes.screens.addfriends

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders

import com.example.boxes.R
import com.example.boxes.databinding.FragmentAddFriendsBinding

class AddFriendsFragment: Fragment() {

    private lateinit var viewModel: AddFriendsViewModel

    private lateinit var binding: FragmentAddFriendsBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_add_friends, container, false)

        viewModel = ViewModelProviders.of(this).get(AddFriendsViewModel::class.java)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        viewModel.inviteFriend.observe(this, Observer {state ->
            if(state) {
                val username = binding.usernameEdittextAddfriends.text.toString()
                if (username.isEmpty()) {
                    Toast.makeText(activity, "Please enter an username to invite a friend", Toast.LENGTH_SHORT)
                        .show()
                }else
                    viewModel.invite(username)
            }
        })

        viewModel.inviteToast.observe(this, Observer { message ->
            Toast.makeText(activity, message, Toast.LENGTH_SHORT).show()
        })

        return binding.root
    }

}
