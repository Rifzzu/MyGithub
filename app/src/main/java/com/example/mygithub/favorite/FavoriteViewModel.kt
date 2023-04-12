package com.example.mygithub.favorite

import android.app.Application
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mygithub.database.UserRepository
import com.example.mygithub.database.User

class FavoriteViewModel(application: Application) : ViewModel() {
    private val userRepo : UserRepository = UserRepository(application)
    fun getFavorite() : LiveData<List<User>> = userRepo.getAllFavorites()

}