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
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder


class AddFriendsViewModel: ViewModel() {

    val adapter = GroupAdapter<ViewHolder>()

    val discardedInvitationsList = mutableSetOf<String?>()

    val currentUserUid = FirebaseAuth.getInstance().uid

    private var _inviteFriend = MutableLiveData<Boolean>()
    val inviteFriend: LiveData<Boolean>
        get() = _inviteFriend

    private var _inviteToast = MutableLiveData<String>()
    val inviteToast: LiveData<String>
        get() = _inviteToast

    private var _recyclerAdapter = MutableLiveData<GroupAdapter<ViewHolder>>()
    val recyclerAdapter: LiveData<GroupAdapter<ViewHolder>>
        get() = _recyclerAdapter

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
                    }

                    checkCurrentUsername(uid, usernameToInvite)

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

    private fun checkCurrentUsername(uid: String, _usernameToInvite:String) {
        val ref = FirebaseDatabase.getInstance().getReference("/users/$currentUserUid")

        ref.addListenerForSingleValueEvent(object: ValueEventListener {

            override fun onDataChange(dataSnapshot: DataSnapshot) {
                val user = dataSnapshot.getValue(User::class.java)
                if(_usernameToInvite != user?.username)
                    sendInvite(user?.username, uid, user!!.profileImageUrl)
                else
                    _inviteToast.value = "You can't invite yourself"
            }

            override fun onCancelled(databaseError: DatabaseError) {
                Log.d("oncancelled", "loadPost:onCancelled", databaseError.toException())
            }
        })
    }

    private fun sendInvite(_userUsername:String?, _uid:String?, userImageUrl:String){
        val Invref = FirebaseDatabase.getInstance().getReference("/users/$_uid/invitations").push()
        val ref = FirebaseDatabase.getInstance().getReference("/users/$_uid/invitations").orderByChild("username").equalTo(_userUsername)
        val invitation = Invitation(_userUsername, userImageUrl,  _uid)

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

    fun fetchInvitations(){
        val ref = FirebaseDatabase.getInstance().getReference("/users/$currentUserUid/invitations")
        ref.addValueEventListener(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                if (p0.exists()) {
                    p0.children.forEach {
                        Log.d("Send", it.toString())
                        //getting value mapped like Invitation class
                        val invitation = it.getValue(Invitation::class.java)
                        if (invitation != null)
                            adapter.add(InvitationItem(invitation) { inv: Invitation -> addToDiscardArray(inv.username) })
                    }
                    _recyclerAdapter.value = adapter
                }
            }
            override fun onCancelled(p0: DatabaseError) {
                Log.d("oncancelled", p0.toString())
            }
        })
    }

    private fun addToDiscardArray(_username:String?){
        discardedInvitationsList.add(_username)
        for(i in discardedInvitationsList) {
            Log.d("AddFriendsViewModel", i.toString())
        }
    }

    fun discardInvites(){
        for(i in discardedInvitationsList){
            val remRef = FirebaseDatabase.getInstance().getReference("/users/$currentUserUid/invitations")
            val ref = FirebaseDatabase.getInstance().getReference("/users/$currentUserUid/invitations").orderByChild("username").equalTo(i)
            ref.addValueEventListener(object: ValueEventListener {

                override fun onDataChange(dataSnapshot: DataSnapshot) {

                    dataSnapshot.children.forEach {
                        val key: String = it.key.toString()
                        remRef.child(key).removeValue()
                    }
                }

                override fun onCancelled(p0: DatabaseError) {
                    Log.d("oncancelled", p0.toString())
                }
            })
        }
    }

    init{
        _inviteFriend.value = false
        _recyclerAdapter.value = adapter
    }

}