package com.example.tp1.ui.screens

import android.net.Uri
import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material3.Button
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.TextFieldValue
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import coil.compose.rememberImagePainter
import com.example.tp1.ui.components.CheckSnack
import com.example.tp1.ui.components.ColorPicker
import com.example.tp1.ui.components.DatePicker
import com.example.tp1.ui.components.DatePicker
import com.example.tp1.ui.components.MyImageArea
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.ResultBackNavigator
import java.text.Normalizer.Form
import java.text.SimpleDateFormat
import java.util.Locale


enum class ProductType{
    Consommable,
    Durable,
    Autre
}

@com.ramcosta.composedestinations.annotation.Destination
@Composable
fun ViewB(
    navigator: DestinationsNavigator,
    name: String,
    resultNavigator: ResultBackNavigator<String>){

    var color by remember { mutableStateOf(Color.Black) }
    var selectedImageUri by remember { mutableStateOf<Uri?>(null)}
    var state by remember { mutableStateOf(ProductType.Consommable) }
    val context = LocalContext.current
    var value1 by remember { mutableStateOf("Alain") }
    var value2 by remember { mutableStateOf("Delon") }
    var date by remember { mutableStateOf<Long?>(0) }
    var showDatePicker by remember { mutableStateOf(false)}

    val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment =  Alignment.CenterHorizontally
    ) {

        // Affichage de l'image

        selectedImageUri?.let {
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = it,
                    modifier = Modifier.size(
                        160.dp
                    ),
                    contentDescription = null,
                )
            }
            Spacer(modifier = Modifier.height(16.dp))
        }

        MyImageArea(
            uri = selectedImageUri,
            directory = context.cacheDir,
            onSetUri = { uri -> selectedImageUri = uri },
        )

        Row(Modifier.selectableGroup()) {
            for (productType in ProductType.entries) {
                Row(Modifier.selectableGroup()) {
                    RadioButton(
                        selected = state == productType,
                        onClick = { state = productType }
                    )
                    Text(productType.toString())
                }
            }
        }

        // Affichage de la couleur
        ColorPicker { c ->
            color = c
        }

        Box(modifier = Modifier
            .size(70.dp)
            .background(color))



        // TextFields

        TextField(
            value = value1,
            onValueChange= { value1 = it },
            label = { Text("Nom*") }
        )
        TextField(
            value = value2,
            onValueChange= { value2 = it },
            label = { Text("Prénom*") }
        )


            TextField(
                modifier = Modifier
                    .clickable {
                        showDatePicker = true
                    }
                    .padding(8.dp),
                value = dateFormat.format(date),
                onValueChange = {},
                label = { Text("Date d'achat") },
                enabled = false
            )


        if(showDatePicker){
            DatePicker{d ->
                date = d
            }
        }


        Button(
            onClick = {
                if (value1.isNotBlank() && value2.isNotBlank()) {
                    val textbox = listOf(value1, value2, color, date,  state.toString())

                    Toast.makeText(
                        context, "Le produit de type " + state + " a été ajouté par " +
                                textbox[0] + " " + textbox[1],
                        Toast.LENGTH_LONG
                    ).show()
                    resultNavigator.navigateBack(textbox.joinToString(separator = ";"))
                } else {
                    Toast.makeText(context, "Veuillez remplir tous les champs", Toast.LENGTH_SHORT).show()
                }
            }
        ) {
            Text("Valider")
        }

    }
}
