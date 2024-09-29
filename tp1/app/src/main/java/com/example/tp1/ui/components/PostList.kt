package com.example.tp1.ui.components

import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.BasicText
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import com.example.tp1.viewmodel.MainViewModel

@Composable
fun PostList(viewModel: MainViewModel) {
    val posts  = viewModel.posts.value
    LazyColumn {
        items(posts) { post ->
            BasicText(text = post.title)
        }
    }
    LaunchedEffect(Unit) {
        viewModel.getPosts()
    }
}