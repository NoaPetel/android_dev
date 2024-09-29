package com.example.tp1.ui.screens

import android.util.Log
import android.widget.Toast
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.combinedClickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import com.example.tp1.ui.screens.destinations.ViewBDestination
import com.ramcosta.composedestinations.annotation.Destination
import com.ramcosta.composedestinations.annotation.RootNavGraph
import com.ramcosta.composedestinations.navigation.DestinationsNavigator
import com.ramcosta.composedestinations.result.NavResult
import com.ramcosta.composedestinations.result.ResultRecipient


data class Product(
    val name: String,
    val surname: String,
    val color: Color,
    val date: String,
    val state: String
)

@Destination(start = true)
@Composable
fun ViewA(navigator: DestinationsNavigator, resultRecipient: ResultRecipient<ViewBDestination, String>){

    val context = LocalContext.current
    var productList: List<Product> by rememberSaveable { mutableStateOf(listOf<Product>())}

    resultRecipient.onNavResult { result ->
        when (result) {
            is NavResult.Value -> {
                val receivedData = result.value.split(";")
                Log.d("ViewA", result.value)

                val newProduct = Product(
                    name = receivedData[0],
                    surname = receivedData[1],
                    color = parseColorString(receivedData[2]),
                    date = receivedData[3],
                    state = receivedData[4]
                )

                Log.d("ViewA", "Produits : ${productList}")

                productList = productList + newProduct
                Log.d("ViewA", "Produits : ${productList}")
                productList = productList.toMutableList()
            }
            is NavResult.Canceled -> {
                Log.d("ViewA", "Navigation error")
            }
        }
    }

    Column(
        modifier = Modifier.fillMaxSize(),
        verticalArrangement = Arrangement.Center,
        horizontalAlignment =  Alignment.CenterHorizontally
    ) {
    LazyVerticalGrid(
        columns = GridCells.Adaptive(minSize = 128.dp)
    ) {
        items(productList) { product ->
            ProductItem(
                product,
                onItemClick = {Toast.makeText(
                    context,
                    "${product.name} sélectionné",
                    Toast.LENGTH_SHORT
                ).show()},
                onItemLongClick = {
                    productList = productList.filter { it != product }

                }
                )
        }
    }


        Button(
            onClick = {
                navigator.navigate(ViewBDestination("test"))
            },
            modifier = Modifier.padding(16.dp)
        ) {
            Text("Cliquez pour accéder au formulaire")
        }
    }


}

// Composable pour afficher un produit
@OptIn(ExperimentalFoundationApi::class)
@Composable
fun ProductItem(product: Product, onItemClick: () -> Unit, onItemLongClick: () -> Unit) {
    Card(
        modifier = Modifier
            .padding(16.dp)
            .fillMaxWidth()
            .combinedClickable(
            onClick = {
                // Ajoutez un log ici pour le clic simple
                Log.d("ProductItem", "Clic sur le produit : ${product.name}")
                onItemClick()
            },
            onLongClick = {
                // Ajoutez un log ici pour le clic long
                Log.d("ProductItem", "Long clic sur le produit : ${product.name}")
                onItemLongClick()
    }
    )
    ) {
        Column(
            modifier = Modifier.padding(16.dp)
        ) {
            Text(text = "Nom: ${product.name}", fontWeight = FontWeight.Bold)
            Text(text = "Prénom: ${product.surname}")
            Box(modifier = Modifier
                .size(30.dp)
                .background(product.color))
            Text(text = "Date: ${product.date}")
            Text(text = "État: ${product.state}")
        }
    }
}

// Fonction pour convertir la chaîne en couleur
fun parseColorString(colorString: String): Color {
    // Extrait les valeurs RVBA de la chaîne
    val regex = Regex("Color\\(([^,]+), ([^,]+), ([^,]+), ([^,]+), .*\\)")
    val matchResult = regex.find(colorString)

    return if (matchResult != null) {
        val (r, g, b, a) = matchResult.destructured
        // Convertir les chaînes en Float et créer un objet Color
        Color(r.toFloat(), g.toFloat(), b.toFloat(), a.toFloat())
    } else {
        // Retourne une couleur par défaut si le format est incorrect
        Color.Transparent
    }
}
