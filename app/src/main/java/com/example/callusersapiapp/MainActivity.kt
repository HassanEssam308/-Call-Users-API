package com.example.callusersapiapp

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import android.os.Build
import android.os.Bundle
import android.widget.Button
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.Toast
import androidx.activity.enableEdgeToEdge
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.view.ViewCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.isGone
import androidx.core.view.isVisible
import androidx.lifecycle.LiveData
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.callusersapiapp.users.UsersResponse
import com.example.callusersapiapp.users.UsersResponseItem
import com.google.android.material.textfield.TextInputEditText
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainActivity : AppCompatActivity() {
    private lateinit var recViewOfUsers: RecyclerView
    private lateinit var loadingLayout: LinearLayout
    private lateinit var imgNoData: ImageView
    private lateinit var btnSearch: Button
    private lateinit var btnGetAllUsers: Button
    private lateinit var etSearch: TextInputEditText


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContentView(R.layout.activity_main)
        ViewCompat.setOnApplyWindowInsetsListener(findViewById(R.id.main)) { v, insets ->
            val systemBars = insets.getInsets(WindowInsetsCompat.Type.systemBars())
            v.setPadding(systemBars.left, systemBars.top, systemBars.right, systemBars.bottom)
            insets
        }
        loadingLayout = findViewById(R.id.IncludeLoadingLinearLayout)
        imgNoData = findViewById(R.id.MainNoDataFoundImg)
        btnSearch = findViewById(R.id.MainBtnSearch)
        etSearch = findViewById(R.id.MainEtSearch)
        btnGetAllUsers = findViewById(R.id.MainBtnGetAllUsers)

        recViewOfUsers = findViewById(R.id.MainRecViewOfUsers)
        recViewOfUsers.layoutManager = LinearLayoutManager(this, RecyclerView.VERTICAL, false)

        checkPermission()

        btnSearch.setOnClickListener {
            val searchText = etSearch.text.toString()

            if (searchText.trim().isEmpty()) {
                Toast.makeText(
                    this,
                    "Please Enter Search Name", Toast.LENGTH_SHORT
                ).show()
            } else {
                getAllUsersByUserName(searchText)
                etSearch.setText("")
            }
            etSearch.clearFocus()
        }
        btnGetAllUsers.setOnClickListener {
            etSearch.clearFocus()
            getAllUsers()
        }
    }


    private fun getAllUsers() {

        loadingLayout.isVisible = true
        RetrofitHelper.getInstance().getUsers().enqueue(object : Callback<UsersResponse> {
            override fun onResponse(
                call: Call<UsersResponse>, response: Response<UsersResponse>
            ) {

                if (response.isSuccessful) {

                    val usersResponse: ArrayList<UsersResponseItem> = response.body()!!
                    checkData(usersResponse)


                } else {
                    Toast.makeText(
                        this@MainActivity, "The Response is not Successful", Toast.LENGTH_SHORT
                    ).show()
                }
            }

            override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                Toast.makeText(this@MainActivity, "Throwable:${t.message}", Toast.LENGTH_LONG)
                    .show()


            }

        })
    }

    private fun getAllUsersByUserName(userName: String) {
        loadingLayout.isVisible = true
        recViewOfUsers.isGone = true
        RetrofitHelper.getInstance()
            .getUsersByUsername(userName)
            .enqueue(object : Callback<UsersResponse> {
                override fun onResponse(
                    call: Call<UsersResponse>, response: Response<UsersResponse>
                ) {

                    if (response.isSuccessful) {

                        val usersResponse: ArrayList<UsersResponseItem> = response.body()!!
                        loadingLayout.isGone = true

                        checkData(usersResponse)


                    } else {
                        Toast.makeText(
                            this@MainActivity, "The Response is not Successful", Toast.LENGTH_SHORT
                        ).show()
                    }
                }

                override fun onFailure(call: Call<UsersResponse>, t: Throwable) {
                    Toast.makeText(this@MainActivity, "Throwable:${t.message}", Toast.LENGTH_LONG)
                        .show()


                }

            })
    }

    private fun checkData(usersResponse: ArrayList<UsersResponseItem>) {

        if (usersResponse.size > 0) {
            recViewOfUsers.adapter = UserAdapter(usersResponse)
            loadingLayout.isGone = true
            imgNoData.isGone = true
            recViewOfUsers.isVisible = true

        } else {
            recViewOfUsers.isGone = true
            imgNoData.isVisible = true
        }

    }

    private fun checkPermission() {

        if (ActivityCompat.checkSelfPermission(
                this,
                Manifest.permission.INTERNET
            ) != PackageManager.PERMISSION_GRANTED
        ) {

            ActivityCompat.requestPermissions(
                this, arrayOf(Manifest.permission.INTERNET), 101
            )

        } else {
            if (checkForInternet()) {
                getAllUsers()
            } else {
                Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
            }

        }
//            val viewModel: MyViewModel by viewModels()
//            viewModel.isOnline.observe(this) { isOnline ->
//                if (isOnline as Boolean) {
//                    getAllUsers()
//                } else {
//                    Toast.makeText(this, "No Internet Connection", Toast.LENGTH_SHORT).show()
//                }
//            }
//        }


    }

    private fun checkForInternet(): Boolean {

        //First, we should register the activity with the ConnectivityManager service.

        val connectivityManager =
            this.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager

        // Returns a Network object corresponding to
        // the currently active default data network.
        val network = connectivityManager.activeNetwork ?: return false

        // Representation of the capabilities of an active network.
        val activeNetwork = connectivityManager.getNetworkCapabilities(network) ?: return false

        return when {
            // Indicates this network uses a Wi-Fi transport,
            // or WiFi has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) -> true

            // Indicates this network uses a Cellular transport. or
            // Cellular has network connectivity
            activeNetwork.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) -> true

            else -> false
        }
    }

}

