package com.example.boxes.screens.send

import com.example.boxes.R
import com.example.boxes.User
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.user_row_send.view.*

class UserItem(val user: User): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.itemView.textview_friend_username.text = user.username

        Picasso.get().load(user.profileImageUrl).into(viewHolder.itemView.image_friend_avatar)
    }

    override fun getLayout(): Int {
        return R.layout.user_row_send
    }
}