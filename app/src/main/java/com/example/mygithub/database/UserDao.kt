package com.example.mygithub.database

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
@Dao
interface UserDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    fun insertFavorite(user: User)

    @Query("DELETE FROM User WHERE User.id = :id")
    fun removeFavorite(id: Int)

    @Query("SELECT * FROM User ORDER BY login ASC")
    fun getAllUser(): LiveData<List<User>>
}