package com.example.githubuserssubmissionfinal.db

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.example.githubuserssubmissionfinal.response.Users

@Database(entities = [Users::class], version = 2)
abstract class FavRoomDatabase : RoomDatabase(){
    abstract fun favDao(): FavDao

    companion object {
        @Volatile
        private var INSTANCE : FavRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavRoomDatabase {
            if (INSTANCE == null) {
                synchronized(FavRoomDatabase::class.java) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        FavRoomDatabase::class.java, "fav_database").fallbackToDestructiveMigration().build()
                }
            }
            return INSTANCE as FavRoomDatabase
        }
    }
}