package com.example.callusersapiapp

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class RetrofitHelper {
    companion object{
        private const val baseUrl = "https://jsonplaceholder.typicode.com/"

        fun getInstance(): ApiInterface {
            return  Retrofit
                .Builder()
                .addConverterFactory(GsonConverterFactory.create())
                .baseUrl(baseUrl)
                .build()
                .create(ApiInterface::class.java)

        }
    }


}