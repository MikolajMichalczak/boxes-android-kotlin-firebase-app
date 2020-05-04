package com.example.boxes.screens.send

import android.os.Bundle
import android.util.Log
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.recyclerview.widget.RecyclerView

import com.example.boxes.R
import com.example.boxes.databinding.FragmentSendBinding
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder
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

        var recyclerview = binding.recyclerviewSend

        viewModel.recyclerAdapter.observe(this, Observer {adapter ->
            recyclerview.adapter = adapter
        })


        viewModel.fetchFriends()

        return binding.root
    }

}
