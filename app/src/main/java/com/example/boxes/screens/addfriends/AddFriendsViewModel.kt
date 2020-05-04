package com.example.boxes.screens.addfriends

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.boxes.User
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class AddFriendsViewModel: ViewModel() {

    private var _inviteFriend = MutableLiveData<Boolean>()
    val inviteFriend: LiveData<Boolean>
        get() = _inviteFriend

    private var _inviteToast = MutableLiveData<String>()
    val inviteToast: LiveData<String>
        get() = _inviteToast

    fun inviteFriend(){
        _inviteFriend.value = true
    }

    fun invite(_username:String){
        val ref = FirebaseDatabase.getInstance().getReference("/users").orderByChild("username").equalTo(
            _username)
        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                var uid = ""
                var usernameToInvite = ""
                if(p0.exists()) {
                    p0.children.forEach {
                        uid = it.child("uid").value.toString()
                        usernameToInvite = it.child("username").value.toString()
                        //Log.d("AddFriendsViewModel", uid)
                    }

                    getCurrentUsername(uid, usernameToInvite)

                }
                else
                    _inviteToast.value = "No user with this username"
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("oncancelled", "loadPost:onCancelled", p0.toException())
            }
        })
        _inviteFriend.value = false
    }

    private fun getCurrentUsername(uid: String, _usernameToInvite:String) {
        val currentUserUid = FirebaseAuth.getInstance().uid
        val ref = FirebaseDatabase.getInstance().getReference("/users/$currentUserUid")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                if(_usernameToInvite != user?.username)
                    sendInvite(user?.username, uid)
                else
                    _inviteToast.value = "You can't invite yourself"
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("oncancelled", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    private fun sendInvite(_userUsername:String?, _uid:String?){
        val Invref = FirebaseDatabase.getInstance().getReference("/users/$_uid/invitations").push()
        val ref = FirebaseDatabase.getInstance().getReference("/users/$_uid/invitations").orderByChild("username").equalTo(_userUsername)
        val invitation = Invitation(_userUsername)

        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(p0: DataSnapshot) {
                if(p0.exists()) {
                    _inviteToast.value = "You have already sent an invitation to this user"
                }
                else {
                    Invref.setValue(invitation)
                        .addOnSuccessListener {
                            _inviteToast.value = "Invite sended"
                        }
                        .addOnFailureListener {
                            Log.d("RegisterViewModel", "${it.message}")
                        }
                }
            }

            override fun onCancelled(p0: DatabaseError) {
                Log.d("oncancelled", "loadPost:onCancelled", p0.toException())
            }
        })
    }

    data class Invitation(val username: String?){
        constructor(): this("")
    }

    init{
        _inviteFriend.value = false
    }

    override fun onCleared() {
        super.onCleared()
    }
}