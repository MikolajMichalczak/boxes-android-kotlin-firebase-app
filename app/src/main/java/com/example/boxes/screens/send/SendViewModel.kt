package com.example.boxes.screens.send

import android.util.Log
import android.view.View
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.boxes.User
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import com.xwray.groupie.GroupAdapter
import com.xwray.groupie.ViewHolder

class SendViewModel: ViewModel() {

    private var _recyclerAdapter = MutableLiveData<GroupAdapter<ViewHolder>>()
    val recyclerAdapter: LiveData<GroupAdapter<ViewHolder>>
        get() = _recyclerAdapter

    fun fetchFriends(){
        val ref = FirebaseDatabase.getInstance().getReference("/users")
        ref.addListenerForSingleValueEvent(object: ValueEventListener{

            override fun onDataChange(p0: DataSnapshot) {
                val adapter = GroupAdapter<ViewHolder>()

                p0.children.forEach{
                    Log.d("Send", it.toString())
                    //getting value mapped like User class
                    val user = it.getValue(User::class.java)
                    if(user != null)
                    adapter.add(UserItem(user))
                }
                _recyclerAdapter.value = adapter
            }
            override fun onCancelled(p0: DatabaseError) {
                Log.d("oncancelled", p0.toString())
            }
        })
    }

    init{

    }

    override fun onCleared() {
        super.onCleared()
    }
}