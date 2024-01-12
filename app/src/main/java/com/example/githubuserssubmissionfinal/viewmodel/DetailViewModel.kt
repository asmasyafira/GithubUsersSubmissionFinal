package com.example.githubuserssubmissionfinal.viewmodel

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserssubmission.BuildConfig
import com.example.githubuserssubmissionfinal.repository.FavRepository
import com.example.githubuserssubmissionfinal.response.DetailUserResponse
import com.example.githubuserssubmissionfinal.response.Users
import com.example.githubuserssubmissionfinal.retrofit.ApiConfig
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {

    private val _userdetail = MutableLiveData<DetailUserResponse>()
    val userdetails: LiveData<DetailUserResponse> = _userdetail

    private val _loadingDetail = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _loadingDetail

    private val _followers = MutableLiveData<List<Users>>()
    val followers: LiveData<List<Users>> = _followers

    private val _followings = MutableLiveData<List<Users>>()
    val followings: LiveData<List<Users>> = _followings

    private val favRepository: FavRepository = FavRepository(application)
    fun allList(): LiveData<List<Users>> = favRepository.getAllFavorite()

    var dataUser: String = ""
    set(value) {
        field = value
        getDetailUser()
        getFollowers()
        getFollowings()
    }

    fun insert(users: Users) {
        favRepository.insert(users)
    }

    fun delete(users: Users) {
        favRepository.delete(users)
    }

    private fun getDetailUser(){
        _loadingDetail.value = true
        val api = ApiConfig.getApiService().getDetailUser(dataUser, BuildConfig.KEY)
        api.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(call: Call<DetailUserResponse>, response: Response<DetailUserResponse>) {
                _loadingDetail.value = false
                _userdetail.value = if (response.isSuccessful) response.body()!! else null
                if (!response.isSuccessful) {
                    Log.e(MainViewModel.TAG, "error: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                _loadingDetail.value = false
                Log.e(MainViewModel.TAG, "error: ${t.message}")
            }
        })
    }

    private fun getFollowers(){
        _loadingDetail.value = true
        val api = ApiConfig.getApiService().getUserFollowers(dataUser, BuildConfig.KEY)
        api.enqueue(object : Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                _loadingDetail.value = false
                _followers.value = if (response.isSuccessful) response.body()!! else null
                if (!response.isSuccessful) {
                    Log.e(MainViewModel.TAG, "error: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                _loadingDetail.value = false
                Log.e(MainViewModel.TAG, "error: ${t.message}")
            }
        })
    }

    private fun getFollowings(){
        _loadingDetail.value = true
        val api = ApiConfig.getApiService().getUserFollowings(dataUser, BuildConfig.KEY)
        api.enqueue(object : Callback<List<Users>> {
            override fun onResponse(call: Call<List<Users>>, response: Response<List<Users>>) {
                _loadingDetail.value = false
                _followings.value = if (response.isSuccessful) response.body()!! else null
                if (!response.isSuccessful) {
                    Log.e(MainViewModel.TAG, "error: ${response.message()}")
                }
            }
            override fun onFailure(call: Call<List<Users>>, t: Throwable) {
                _loadingDetail.value = false
                Log.e(MainViewModel.TAG, "error: ${t.message}")
            }
        })
    }
}