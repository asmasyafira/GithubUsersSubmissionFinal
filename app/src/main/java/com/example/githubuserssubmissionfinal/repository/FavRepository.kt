package com.example.githubuserssubmissionfinal.repository

import android.app.Application
import androidx.lifecycle.LiveData
import com.example.githubuserssubmissionfinal.db.FavDao
import com.example.githubuserssubmissionfinal.db.FavRoomDatabase
import com.example.githubuserssubmissionfinal.response.Users
import java.util.concurrent.ExecutorService
import java.util.concurrent.Executors

class FavRepository(application: Application) {
    private val mFavDao: FavDao
    private val executorService: ExecutorService = Executors.newSingleThreadExecutor()

    init {
        val db = FavRoomDatabase.getDatabase(application)
        mFavDao = db.favDao()
    }

    fun getAllFavorite(): LiveData<List<Users>> = mFavDao.getFavUser()

    fun insert(user: Users) {
        executorService.execute { mFavDao.insert(user) }
    }

    fun delete(user: Users) {
        executorService.execute { mFavDao.delete(user) }
    }
}