package com.example.boxes.screens.addfriends
import android.view.View
import com.example.boxes.R
import com.squareup.picasso.Picasso
import com.xwray.groupie.Item
import com.xwray.groupie.ViewHolder
import kotlinx.android.synthetic.main.invitation_row_invite.view.*

class InvitationItem (val invitation: Invitation, val onInvitationDiscardClick: (Invitation) -> Unit): Item<ViewHolder>() {
    override fun bind(viewHolder: ViewHolder, position: Int) {

        viewHolder.itemView.textview_friend_username.text = invitation.username

        viewHolder.itemView.invite_discard_button.setOnClickListener{
            onInvitationDiscardClick(invitation)
            viewHolder.itemView.rounded_textview.visibility = View.VISIBLE
            viewHolder.itemView.invite_accept_button.visibility = View.GONE
            viewHolder.itemView.invite_discard_button.visibility = View.GONE
            viewHolder.itemView.rounded_textview.setText(R.string.rejected)
        }

        Picasso.get().load(invitation.userImageUrl).into(viewHolder.itemView.image_friend_avatar)
    }

    override fun getLayout(): Int {
        return R.layout.invitation_row_invite
    }

}


