package com.example.boxes.screens.login

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth

class LoginViewModel : ViewModel() {

    private var _toRegister = MutableLiveData<Boolean>()
    val toRegister: LiveData<Boolean>
        get() = _toRegister

    private var _getPassAndEmail = MutableLiveData<Boolean>()
    val getPassAndEmail: LiveData<Boolean>
        get() = _getPassAndEmail

    private var _toInbox = MutableLiveData<Boolean>()
    val toInbox: LiveData<Boolean>
        get() = _toInbox

    fun toRegisterFragment() {
        _toRegister.value = true
    }

    fun endNavigateToRegisterFragment() {
        _toRegister.value = false
    }

    fun getEmailAndPass() {
        _getPassAndEmail.value = true
    }

    fun endNavigateToInboxFragment(){
        _toInbox.value = false
    }


    fun login(email: String, password: String) {
        FirebaseAuth.getInstance().signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(){
                _toInbox.value = true
            }
            .addOnFailureListener{

            }
        _getPassAndEmail.value = false
    }

    init {
        _toRegister.value = false
        _getPassAndEmail.value = false
        _toInbox.value = false
    }

    override fun onCleared() {
        super.onCleared()
    }
}