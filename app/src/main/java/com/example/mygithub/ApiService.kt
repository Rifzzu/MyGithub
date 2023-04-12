package com.example.mygithub

import retrofit2.Call
import retrofit2.http.*

interface ApiService {

    @GET("users/{username}")
    fun getDetail(
        @Path("username") username: String,
        @Header("Authorization") token: String = "token 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"
    ): Call<DetailUserResponse>

    @GET("/users/{username}/followers")
    fun getFollowers(
        @Path("username") username: String,
        @Header("Authorization") token: String = "token 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"
    ): Call<List<ItemsItem>>

    @GET("/users/{username}/following")
    fun getFollowing(@Path("username") username: String,
                             @Header("Authorization") token: String = "token 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"
    ): Call<List<ItemsItem>>

    @GET("search/users")
    fun searchUser(
        @Query("q") query: String,
        @Header("Authorization") token: String = "token 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"
    ): Call<GithubResponse>

    @GET("users")
    fun getUsersList(): Call<List<ItemsItem>>
}