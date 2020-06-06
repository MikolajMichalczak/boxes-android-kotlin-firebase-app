package com.example.boxes.screens.addfriends

data class Invitation(val username: String?, val userImageUrl:String, val uid: String?){
    constructor(): this("", "", "")
}