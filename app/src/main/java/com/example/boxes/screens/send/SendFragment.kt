package com.example.boxes.screens.send

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.ViewModelProviders

import com.example.boxes.R
import com.example.boxes.databinding.FragmentSendBinding
import kotlinx.android.synthetic.main.fragment_send.*


class SendFragment : Fragment() {

    private lateinit var viewModel: SendViewModel

    private lateinit var binding: FragmentSendBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_send, container, false)

        viewModel = ViewModelProviders.of(this).get(SendViewModel::class.java)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        recyclerview_send.adapter
        
        return binding.root
    }

}
