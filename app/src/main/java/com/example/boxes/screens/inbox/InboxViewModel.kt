package com.example.boxes.screens.inbox

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.FirebaseApp

class InboxViewModel: ViewModel() {

    private var _toRegister = MutableLiveData<Boolean>()
    val toRegister: LiveData<Boolean>
        get() = _toRegister

    private fun checkAuth(){
        val uid = FirebaseAuth.getInstance().uid
        if(uid == null) {
            _toRegister.value = true
        }
    }

    fun endNavigateToRegisterFragment(){
        _toRegister.value = false
    }

    init {
        //FirebaseAuth.getInstance().signOut()
        _toRegister.value = false
        checkAuth()
    }

    override fun onCleared() {
        super.onCleared()
    }
}