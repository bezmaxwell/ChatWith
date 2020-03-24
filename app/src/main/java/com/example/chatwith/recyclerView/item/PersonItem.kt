package com.example.chatwith.recyclerView.item

import android.content.Context
import com.bumptech.glide.Glide
import com.example.chatwith.R
import com.xwray.groupie.kotlinandroidextensions.Item
import com.xwray.groupie.kotlinandroidextensions.ViewHolder
import kotlinx.android.synthetic.main.fragment_my_account.*
import kotlinx.android.synthetic.main.item_person.*


class PersonItem(val person:com.example.chatwith.model.User,
                 val userId: String,
                 private val context: Context):
        Item() {

    override fun bind(viewHolder: ViewHolder, position: Int) {
        viewHolder.textView_name.text = person.name
        viewHolder.textView_bio.text = person.bio
        if (person.profilePicturePath !=null)
            Glide.with(context)
                .load(person.profilePicturePath)
                .into(viewHolder.imageView_profile_picture)
    }

    override fun getLayout() = R.layout.item_person

}