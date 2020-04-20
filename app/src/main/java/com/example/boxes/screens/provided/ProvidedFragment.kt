package com.example.boxes.screens.provided

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProviders
import com.example.boxes.R
import com.example.boxes.databinding.FragmentProvidedBinding

class ProvidedFragment : Fragment() {

    private lateinit var viewModel: ProvidedViewModel

    private lateinit var binding: FragmentProvidedBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_provided, container, false)

        viewModel = ViewModelProviders.of(this).get(ProvidedViewModel::class.java)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)


        return binding.root
    }

}
