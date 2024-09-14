package com.example.tp1

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.selection.selectableGroup
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.Button
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExtendedFloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.RadioButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.derivedStateOf
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.tp1.ui.theme.Tp1Theme
import com.github.skydoves.colorpicker.compose.ColorEnvelope
import com.github.skydoves.colorpicker.compose.HsvColorPicker
import com.github.skydoves.colorpicker.compose.rememberColorPickerController
import kotlinx.coroutines.launch
import java.util.Date
import kotlin.io.encoding.Base64


class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        val value = 0;

        var selection : String
        var textbox : Array<String>

        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            Tp1Theme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column() {
                        Picture()
                        selection = Selection()
                        ColorPicker()
                        ComponentDatePicker()
                        textbox = TextBox()
                        Validation(selection, textbox)
                    }
                }
            }
        }
    }
}

@Composable
fun Picture(){
    Image(
        painter = painterResource(id = R.drawable.image),
        contentDescription = stringResource(id = R.string.image)
    )
}

enum class ProductType{
    Consommable,
    Durable,
    Autre
}

@Composable
fun Selection(): String{
    var state by remember { mutableStateOf(ProductType.Consommable)}
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
    return state.toString()
}

@Composable
fun TextBox(): Array<String> {

    var value1 by remember { mutableStateOf("Alain")}
    var value2 by remember { mutableStateOf("Delon")}

    TextField(
        value = value1,
        onValueChange= { value1 = it },
        label = { Text("Nom")}
    )
    TextField(
        value = value2,
        onValueChange= { value2 = it },
        label = { Text("Prénom")}
    )


    return arrayOf(value1, value2)
}

@Composable
fun ColorPicker(){
    val controller = rememberColorPickerController()
    var color by remember { mutableStateOf(Color.Black)}

    HsvColorPicker(
        modifier = Modifier
            .fillMaxWidth()
            .height(300.dp)
            .padding(10.dp),
        controller = controller,
        onColorChanged = { colorEnvelope: ColorEnvelope ->
            color = colorEnvelope.color
        }
    )
    Box(modifier = Modifier.size(100.dp).background(color))

}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ComponentDatePicker(){
    val snackState = remember { SnackbarHostState() }
    val snackScope = rememberCoroutineScope()
    SnackbarHost(hostState = snackState, Modifier)
    val openDialog = remember { mutableStateOf(true) }
    if (openDialog.value) {
        val datePickerState = rememberDatePickerState()
        val confirmEnabled = remember {
            derivedStateOf { datePickerState.selectedDateMillis != null }
        }
        DatePickerDialog(
            onDismissRequest = {
                // Dismiss the dialog when the user clicks outside the dialog or on the back
                // button. If you want to disable that functionality, simply use an empty
                // onDismissRequest.
                openDialog.value = false
            },
            confirmButton = {
                TextButton(
                    onClick = {
                        openDialog.value = false
                        snackScope.launch {
                            snackState.showSnackbar(
                                "Selected date timestamp: ${datePickerState.selectedDateMillis}"
                            )
                        }
                    },
                    enabled = confirmEnabled.value
                ) {
                    Text("OK")
                }
            },
            dismissButton = {
                TextButton(onClick = { openDialog.value = false }) { Text("Cancel") }
            }
        ) {
            DatePicker(state = datePickerState)
            
        }
    }
}

    @Composable
    fun CheckSnack() {
        val scope = rememberCoroutineScope()
        val snackbarHostState = remember { SnackbarHostState() }
        Scaffold(
            snackbarHost = {
                SnackbarHost(hostState = snackbarHostState)
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    text = { Text("Show snackbar") },
                    icon = { Icon(Icons.Filled.Email, contentDescription = "") },
                    onClick = {
                        scope.launch {
                            snackbarHostState.showSnackbar("Snackbar")
                        }
                    }
                )
            }
        ) { paddingValues ->

            Column(modifier = Modifier.padding(paddingValues)) {
                Text("", modifier = Modifier.padding(16.dp))
            }
        }
    }

    @Composable
    fun Validation(selection: String, textbox: Array<String>) {
        val context = LocalContext.current

        if (textbox[0] == "" || textbox[1] == "") {
            CheckSnack()
        } else {
            Button(
                onClick = {
                    Toast.makeText(
                        context, "Le produit de type " + selection + " a été ajouté par " +
                                textbox[0] + " " + textbox[1],
                        Toast.LENGTH_LONG
                    ).show()
                },
            ) {
                Text("Valider")
            }
        }
    }

