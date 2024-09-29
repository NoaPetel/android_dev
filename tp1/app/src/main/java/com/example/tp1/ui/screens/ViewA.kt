package com.example.tp1.ui.screens

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import com.example.tp1.ui.screens.destinations.ViewBDestination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient

@com.ramcosta.composedestinations.annotation.Destination(start = true)
@Composable
fun ViewA(navigator: DestinationsNavigator, resultRecipient: ResultRecipient<ViewBDestination, String>){
    var name by remember { mutableStateOf("") }

    resultRecipient.onNavResult {
        if(it is NavResult.Value){
            name = it.value
        }
    }
    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment =  Alignment.CenterHorizontally
    ) {
        Button(
            onClick = {
                navigator.navigate(ViewBDestination("test"))
            }
        ) {
            Text("Cliquez pour accéder au formulaire")
        }
    }
}