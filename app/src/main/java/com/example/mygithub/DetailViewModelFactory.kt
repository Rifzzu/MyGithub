package com.example.mygithub

import android.app.Application
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.mygithub.favorite.FavoriteViewModel

class DetailViewModelFactory private constructor(private val application: Application): ViewModelProvider.NewInstanceFactory() {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(DetailViewModel::class.java)){
            return DetailViewModel(application) as T
        } else if (modelClass.isAssignableFrom(FavoriteViewModel::class.java)){
            return FavoriteViewModel(application) as T
        }
        throw IllegalArgumentException("Unknown ViewModel Class : &{modelClass.name")
    }

    companion object{
        @Volatile
        private var instance : DetailViewModelFactory? = null

        @JvmStatic
        fun getInstance(application: Application) : DetailViewModelFactory {
            if (instance == null){
                synchronized(DetailViewModelFactory::class.java){
                    instance = DetailViewModelFactory(application)
                }
            }
            return instance as DetailViewModelFactory
        }
    }
}