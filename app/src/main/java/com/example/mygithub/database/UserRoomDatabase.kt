package com.example.mygithub.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(entities = [User::class], version = 2, exportSchema = false)
abstract class FavoriteRoomDatabase : RoomDatabase() {
    abstract fun userDao(): UserDao

    companion object {
        @Volatile
        private var instance: FavoriteRoomDatabase? = null

        @JvmStatic
        fun getDatabase(context: Context): FavoriteRoomDatabase {
            if(instance == null) {
                synchronized(FavoriteRoomDatabase::class.java) {
                    instance = Room.databaseBuilder(
                        context.applicationContext,
                        FavoriteRoomDatabase::class.java, "User_Database")
                        .fallbackToDestructiveMigration()
                        .build()
                }
            }
            return instance as FavoriteRoomDatabase
        }
    }
}