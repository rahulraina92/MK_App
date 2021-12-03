package com.example.milkkhata.screens

import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Done
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import com.example.milkkhata.MainViewModel
import com.example.milkkhata.room.MyRecord

@Composable
fun ListScreen(navController: NavController, vm: MainViewModel) {

    val dialogVisibility = remember { vm.addNewEntryDialogVisibility }

    Column {
        Button(onClick = { dialogVisibility.value = true }) { Text(text = "Add") }
        DrawList(vm = vm)
    }
    if (dialogVisibility.value) AddNewEntryDialog(vm = vm)

}

@Composable
fun DrawList(vm: MainViewModel) {

    val list by remember { mutableStateListOf(vm) }

    LazyColumn(
        modifier = Modifier
            .fillMaxWidth()
            .fillMaxHeight()
    ) {
        items(list) { item ->
            DrawItem(item = item)
            Divider(color = Color.Gray, thickness = 1.dp)
        }
    }
}

@Composable
fun DrawItem(item: MyRecord) {
    Row {
        Text(text = item.toString())
    }
}

@Composable
fun AddNewEntryDialog(vm: MainViewModel) {

    AlertDialog(
        onDismissRequest = { vm.addNewEntryDialogVisibility.value = false },
        title = { Text(text = "New Entry") },
        text = { AddNewEntryDialogBody(vm = vm) },
        confirmButton = {
            HorizontalSpacing(4)
            ExtendedFloatingActionButton(
                onClick = {
                    vm.addNewEntryDialogVisibility.value = false
                    vm.saveRecord()
                },
                text = { Text(text = "  Save    ") },
                icon = { Icon(Icons.Filled.Done, "Save") },
                elevation = FloatingActionButtonDefaults.elevation(2.dp)
            )
            HorizontalSpacing(4)
        },
        dismissButton = {
            ExtendedFloatingActionButton(
                onClick = { vm.addNewEntryDialogVisibility.value = false },
                text = { Text(text = "Dismiss") },
                icon = { Icon(Icons.Filled.Done, "Dismiss") },
                elevation = FloatingActionButtonDefaults.elevation(2.dp)
            )
        }
    )
}

@Composable
fun AddNewEntryDialogBody(vm: MainViewModel) {

    var quantity by remember { vm.quantity }
    var quality by remember { vm.quality }
    var price by remember { vm.price }
    var note by remember { vm.note }

    Column(modifier = Modifier.padding(8.dp)) {
        TextField(
            value = quantity,
            placeholder = { Text("eg: 2.25 (in litre)") },
            label = { Text("Quantity") },
            onValueChange = { quantity = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        VerticalSpacing()
        TextField(
            value = price,
            placeholder = { Text("eg: 122.50 (in Rupees)") },
            label = { Text("Price") },
            onValueChange = { price = it },
            singleLine = true,
            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number)
        )
        VerticalSpacing()
        Slider(
            value = quality,
            onValueChange = { quality = it },
            valueRange = 0F..5F,
            steps = 4
        )
        VerticalSpacing()
        TextField(
            value = note,
            placeholder = { Text("Make a note...") },
            label = { Text("Note") },
            onValueChange = { note = it },
            maxLines = 3,
            singleLine = false
        )
    }
}

@Composable
fun VerticalSpacing() {
    Spacer(modifier = Modifier.height(8.dp))
}

@Composable
fun HorizontalSpacing(dp: Int) {
    Spacer(modifier = Modifier.width(dp.dp))
}