package com.example.githubuserssubmissionfinal.viewmodel

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserssubmission.BuildConfig
import com.example.githubuserssubmissionfinal.response.GithubResponse
import com.example.githubuserssubmissionfinal.response.Users
import com.example.githubuserssubmissionfinal.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MainViewModel : ViewModel() {

    private val _userList = MutableLiveData<List<Users>>()
    val userList: LiveData<List<Users>> = _userList

    private val _user = MutableLiveData<List<Users>>()
    val user: LiveData<List<Users>> = _user

    private val _mainLoading = MutableLiveData<Boolean>()
    val mainLoading: LiveData<Boolean> = _mainLoading

    companion object {
        const val TAG = "ViewModel"
    }

    init {
        findGithub()
    }

    fun findSearchUser(dataUser : String) {
        _mainLoading.value = true
        val service = ApiConfig.getApiService().getSearchUser(dataUser, BuildConfig.KEY)
        service.enqueue(object : Callback<GithubResponse>{
            override fun onResponse(call: Call<GithubResponse>, response: Response<GithubResponse>) {
                _mainLoading.value = false
                val responseBody = response.body()
                if (response.isSuccessful && responseBody != null) {
                    _userList.value = response.body()?.items
                } else {
                    Log.e(TAG, "error: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<GithubResponse>, t: Throwable) {
                _mainLoading.value = false
                Log.e(TAG, "error: ${t.message}")
            }
        })
    }

    private fun findGithub() {
        _mainLoading.value = true
        val service = ApiConfig.getApiService().getUsers(BuildConfig.KEY)
        service.enqueue(object : Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                _mainLoading.value = false
                val responseBody = response.body()
                _user.value = if (response.isSuccessful && responseBody != null) responseBody else null
                if (!response.isSuccessful || responseBody == null) {
                    Log.e(TAG, "error: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                _mainLoading.value = false
                Log.e(TAG, "error: ${t.message}")
            }
        })
    }
}