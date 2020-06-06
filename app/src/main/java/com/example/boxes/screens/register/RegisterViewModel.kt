package com.example.boxes.screens.register

import android.net.Uri
import android.util.Log
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;
import com.example.boxes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.storage.FirebaseStorage
import java.util.*

class RegisterViewModel : ViewModel() {

    private var _toLogin = MutableLiveData<Boolean>()
    val toLogin: LiveData<Boolean>
        get() = _toLogin

    private var _getPassAndEmail = MutableLiveData<Boolean>()
    val getPassAndEmail: LiveData<Boolean>
        get() = _getPassAndEmail

    private var _showFailureToast = MutableLiveData<String>()
    val showFailureToast: LiveData<String>
        get() = _showFailureToast

    private var _goSelectPhoto = MutableLiveData<Boolean>()
    val goSelectPhoto : LiveData<Boolean>
        get() = _goSelectPhoto

    private var _toInbox = MutableLiveData<Boolean>()
    val toInbox: LiveData<Boolean>
        get() = _toInbox


    fun toLoginFragment(){
        _toLogin.value = true
    }

    fun endNavigateToLoginFragment(){
        _toLogin.value = false
    }

    fun getEmailAndPass(){
        _getPassAndEmail.value = true
    }

    var selectedPhotoUri: Uri? = null

    fun register(email: String, password: String, username: String){

        FirebaseAuth.getInstance().createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener{
                if(!it.isSuccessful) return@addOnCompleteListener
                Log.d("RegisterViewModel", "created user with id: ${it.result?.user?.uid}")
                uploadImageToFirebaseStorage(username)
            }
            .addOnFailureListener{
                Log.d("RegisterViewModel", "Failed to create user: ${it.message}")
                _showFailureToast.value = "Failed to create user: ${it.message}"
            }
        _getPassAndEmail.value = false
    }

    fun startSelectPhoto(){
       _goSelectPhoto.value = true
    }

    fun endSelectPhoto(){
        _goSelectPhoto.value = false
    }

    fun endNavigateToInboxFragment(){
        _toInbox.value = false
    }

    private fun uploadImageToFirebaseStorage(username:String){

        val filename = UUID.randomUUID().toString()
        val ref = FirebaseStorage.getInstance().getReference("/images/$filename")

        if(selectedPhotoUri == null) {
            saveUserToFirebase("https://firebasestorage.googleapis.com/v0/b/boxes-44d7a.appspot.com/o/images%2F8f646f78-2f2a-441f-ba46-3d78bfbf9a1c?alt=media&token=e43eae83-83c8-41b4-a538-9a38e68ff471", username)
        }
        else
            ref.putFile(selectedPhotoUri!!)
                .addOnSuccessListener {
                    Log.d("RegisterViewModel", "Image added to firebase (${it.metadata?.path})")

                    ref.downloadUrl.addOnSuccessListener {
                        saveUserToFirebase(it.toString(), username)
                    }
                }
                .addOnFailureListener {
                    Log.d("RegisterViewModel", "${it.message}")
                }
    }

    private fun saveUserToFirebase(profileImageUrl: String, username:String){
        val uid = FirebaseAuth.getInstance().uid ?: ""
        val ref = FirebaseDatabase.getInstance().getReference("/users/$uid")

        val user = User(uid, username, profileImageUrl)
        ref.setValue(user)
            .addOnSuccessListener {
                Log.d("RegisterViewModel", "User added to database")
                _toInbox.value = true
            }
            .addOnFailureListener{
                Log.d("RegisterViewModel", "${it.message}")
            }
    }

    init{
        _toLogin.value = false
        _getPassAndEmail.value = false
        _goSelectPhoto.value = false
        _toInbox.value = false
    }

    override fun onCleared() {
        super.onCleared()
    }
}