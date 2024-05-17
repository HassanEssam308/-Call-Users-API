package com.example.callusersapiapp.users

import com.example.callusersapiapp.users.Address
import com.example.callusersapiapp.users.Company


data class UsersResponseItem(
    val address: Address,
    val company: Company,
    val email: String,
    val id: Int,
    val name: String,
    val phone: String,
    val username: String,
    val website: String
)