package com.example.boxes.screens.inbox

import android.os.Bundle
import android.util.Log
import android.view.*
import androidx.databinding.DataBindingUtil
import androidx.fragment.app.Fragment
import androidx.lifecycle.Observer
import androidx.lifecycle.ViewModelProviders
import androidx.navigation.fragment.findNavController
import com.example.boxes.R
import com.example.boxes.databinding.FragmentInboxBinding
import com.example.boxes.screens.addfriends.AddFriendsFragment
import com.example.boxes.screens.recents.RecentsFragment
import com.example.boxes.screens.send.SendFragment
import com.google.firebase.auth.FirebaseAuth


class InboxFragment : Fragment() {

    private lateinit var viewModel: InboxViewModel
    private lateinit var binding: FragmentInboxBinding

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        binding = DataBindingUtil.inflate(
            inflater, R.layout.fragment_inbox, container, false
        )

        viewModel = ViewModelProviders.of(this).get(InboxViewModel::class.java)
        binding.viewModel = viewModel
        binding.setLifecycleOwner(this)

        viewModel.toRegister.observe(this, Observer { state ->
            navigateToRegisterFragment(state)
        })

        setHasOptionsMenu(true);
        binding.bottomNavigation.setOnNavigationItemSelectedListener {
            when(it.itemId){
                R.id.menu_inbox-> {
                    Log.i("Inbox", "Inbox clicked")
                    loadFragment(InboxFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.menu_recents-> {
                    loadFragment(RecentsFragment())
                    return@setOnNavigationItemSelectedListener true
                }

                R.id.menu_send-> {
                    loadFragment(SendFragment())
                    return@setOnNavigationItemSelectedListener true
                }

            }
            false

        }
        return binding.root
    }

    private fun navigateToRegisterFragment(_state: Boolean) {
        if (_state) {
            findNavController().navigate(R.id.action_inboxFragment_to_registerFragment)
            viewModel.endNavigateToRegisterFragment()
        }
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        when (item?.itemId) {
            R.id.menu_sign_out -> {
                FirebaseAuth.getInstance().signOut()
                findNavController().navigate(R.id.action_inboxFragment_to_registerFragment)
            }
            R.id.menu_add_friend -> {
                loadFragment(AddFriendsFragment())
                return true
            }
        }
        return super.onOptionsItemSelected(item)
    }

    override fun onCreateOptionsMenu(menu: Menu, inflater: MenuInflater) {
        menu.clear()
        inflater.inflate(R.menu.nav_menu, menu)
        return super.onCreateOptionsMenu(menu, inflater)
    }

    private fun loadFragment(fragment: Fragment) {
        // load fragment
        val transaction = getActivity()?.getSupportFragmentManager()?.beginTransaction()
        if (transaction != null) {
            transaction.replace(R.id.fragmentContainer, fragment)
            transaction.addToBackStack(null)
            transaction.commit()
        }
    }
}

