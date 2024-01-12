package com.example.bridge_authorized

import android.content.Intent
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import kotlin.system.exitProcess

class AuthorizedPersonAdapter(private val usersList: List<AuthorizedPerson>,
                              private val onDeleteClick: (AuthorizedPerson) -> Unit)
    : RecyclerView.Adapter<AuthorizedPersonAdapter.UserViewHolder>() {

    class UserViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val textViewName: TextView = view.findViewById(R.id.textViewName)
        val buttonDelete: Button = view.findViewById(R.id.buttonDelete)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): UserViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_user, parent, false)
        return UserViewHolder(view)
    }

    override fun onBindViewHolder(holder: UserViewHolder, position: Int) {
        val user = usersList[position]
        holder.textViewName.text = "${user.autname} ${user.autsurname}" // Displaying name and surname
        holder.buttonDelete.setOnClickListener { onDeleteClick(user) }
    }


    override fun getItemCount() = usersList.size
}
