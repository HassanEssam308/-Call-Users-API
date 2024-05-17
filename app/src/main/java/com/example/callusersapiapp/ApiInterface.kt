package com.example.callusersapiapp

import com.example.callusersapiapp.users.UsersResponse
import com.example.callusersapiapp.users.UsersResponseItem
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Path
import retrofit2.http.Query

interface ApiInterface {

    @GET("users")
     fun getUsers(): Call<UsersResponse>

    @GET("users/{id}")
    fun getUserById(@Path("id") userId: Int) :Call<UsersResponseItem>
  //  usershttps://jsonplaceholder.typicode.com/users?username=Bret
    @GET("users")
    fun getUsersByUsername(@Query("username")username:String):Call<UsersResponse>
}