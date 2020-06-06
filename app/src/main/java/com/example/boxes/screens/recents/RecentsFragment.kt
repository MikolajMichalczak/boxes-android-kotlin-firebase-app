package com.example.boxes.screens.recents

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.boxes.R
import com.example.boxes.databinding.FragmentProvidedBinding

class RecentsFragment : Fragment() {

    private lateinit var viewModel: RecentsViewModel

    private lateinit var binding: FragmentProvidedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_provided, container, false)

        viewModel = ViewModelProviders.of(this).get(RecentsViewModel::class.java)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)


        return binding.root
    }

}
