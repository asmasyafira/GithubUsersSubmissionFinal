package com.example.githubuserssubmissionfinal.viewmodel

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.githubuserssubmissionfinal.repository.FavRepository
import com.example.githubuserssubmissionfinal.response.Users

class FavoriteViewModel(application: Application) :ViewModel() {
    private val mNoteRepository: FavRepository = FavRepository(application)

    fun getAllFav(): LiveData<List<Users>> = mNoteRepository.getAllFavorite()
}