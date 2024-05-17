package com.example.callusersapiapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.example.callusersapiapp.users.UsersResponseItem

class UserAdapter(private var arrayList: ArrayList<UsersResponseItem>) :
    RecyclerView.Adapter<UserAdapter.ViewHolder>() {

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        var userName: TextView = itemView.findViewById(R.id.RowUser_UserName)
        var email: TextView = itemView.findViewById(R.id.RowUser_Email)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
       return  ViewHolder(LayoutInflater.from(parent.context).inflate(
           R.layout.row_user_layout,parent,false
       ))
    }

    override fun getItemCount(): Int {
        return arrayList.size
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        holder.userName.text = arrayList[position].username
        holder.email.text = arrayList[position].email

    }


}