package com.example.tp1.viewmodel

import androidx.compose.runtime.MutableState
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.tp1.data.model.Post
import com.example.tp1.data.repository.RetrofitInstance
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class MainViewModel: ViewModel() {

    private val apiService = RetrofitInstance.api
    val posts: MutableState<List<Post>> = mutableStateOf(emptyList())

    fun getPosts() {
        viewModelScope.launch {
            try {
                val response = apiService.getPosts()
                if (response.isNotEmpty()) {
                    posts.value = response
                }
            } catch (e: Exception) {
                // Handle errors here
            }
        }
    }
}