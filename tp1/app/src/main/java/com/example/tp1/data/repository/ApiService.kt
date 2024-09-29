package com.example.tp1.data.repository

import com.example.tp1.data.model.Post
import retrofit2.http.GET


interface ApiService {
    @GET("posts")
    suspend fun getPosts(): List<Post>
}