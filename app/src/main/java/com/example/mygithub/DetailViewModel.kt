package com.example.mygithub

import android.app.Application
import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.mygithub.database.User
import com.example.mygithub.database.UserRepository
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DetailViewModel(application: Application) : ViewModel() {

    private val detail_User = MutableLiveData<DetailUserResponse>()
    val detailUser: LiveData<DetailUserResponse> = detail_User

    private val is_Loading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = is_Loading

    private val follower = MutableLiveData<List<ItemsItem>>()
    val followers: LiveData<List<ItemsItem>> = follower

    private val following = MutableLiveData<List<ItemsItem>>()
    val followings: LiveData<List<ItemsItem>> = following

    private val _listUser = MutableLiveData<ItemsItem>()
    val listUser : LiveData<ItemsItem> = _listUser

    private val _error = MutableLiveData<Boolean>()
    val error : LiveData<Boolean> = _error

    companion object {
        const val TAGS = "DetailViewModel"
    }

    init {
        getUserDetail()
    }

    private val mUserRepository: UserRepository =
        UserRepository(application)

    fun insert(user: User){
        mUserRepository.insert(user)
    }

    fun delete(id: Int){
        mUserRepository.delete(id)
    }

    fun getFavorite():LiveData<List<User>> =
        mUserRepository.getAllFavorites()

    fun getUserDetail(query: String = "") {
        is_Loading.value = true
        val client = ApiConfig.getApiService().getDetail(query)
        client.enqueue(object : Callback<DetailUserResponse> {
            override fun onResponse(
                call: Call<DetailUserResponse>,
                response: Response<DetailUserResponse>,
            ) {
                is_Loading.value = false
                if (response.isSuccessful) {
                    detail_User.value = response.body()
                } else {
                    Log.d(TAGS, "onFailure: ${response.message()}111")
                }
            }

            override fun onFailure(call: Call<DetailUserResponse>, t: Throwable) {
                is_Loading.value = false
                Log.e(TAGS, "onFailure = ${t.message.toString()}")
            }
        })
    }

    fun getFollower(query: String = "") {
        is_Loading.value = true
        val client = ApiConfig.getApiService().getFollowers(query)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>,
            ) {
                is_Loading.value = false
                if (response.isSuccessful) {
                    follower.value = response.body()
                } else {
                    Log.d(TAGS, "onFailure: ${response.message()}222")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                is_Loading.value = false
                Log.e(TAGS, "onFailure : ${t.message.toString()}")
            }
        })
    }

    fun getFollowing(query: String = "") {
        is_Loading.value = true
        val client = ApiConfig.getApiService().getFollowing(query)
        client.enqueue(object : Callback<List<ItemsItem>> {
            override fun onResponse(
                call: Call<List<ItemsItem>>,
                response: Response<List<ItemsItem>>,
            ) {
                is_Loading.value = false
                if (response.isSuccessful) {
                    following.value = response.body()
                } else {
                    Log.d(TAGS, "onFailure: ${response.message()}333")
                }
            }

            override fun onFailure(call: Call<List<ItemsItem>>, t: Throwable) {
                is_Loading.value = false
                Log.e(TAGS, "onFailure : ${t.message.toString()}")
            }
        })
    }
    fun doneToastError(){
        _error.value = false
    }
}