package com.example.shoppinglistapp

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

data class ShoppingItem(
    val id: Int, var name: String, var quantity: Int, var isEditing: Boolean = false
)

@Composable
fun ShoppingListApp() {
    var sItems by remember {
        mutableStateOf(listOf<ShoppingItem>())
    }
    var showDialog by remember {
        mutableStateOf(false)
    }
    var itemName by remember {
        mutableStateOf("")
    }
    var itemQuantity by remember {
        mutableStateOf("")
    }

    // UI Element: Column
    Column(
        modifier = Modifier.fillMaxSize(), verticalArrangement = Arrangement.Center
    ) {
        // UI Element: Button (Used for showing the dialog to add a new item)
        Button(onClick = {
            showDialog = true
        }, modifier = Modifier.align(Alignment.CenterHorizontally)) {

            Text(text = "Add Item")
        }
        // UI Element: LazyColumn (Used for displaying the list of items)
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            // This is a lambda function that is passed to the items() function.
            // The items() function is a part of the LazyColumn Composable and is used to display a list of items.
            items(sItems) { item ->

                // Check if the current item is being edited
                if (item.isEditing) {

                    // If the item is being edited, display the ShoppingItemEditor Composable.
                    // This Composable is used to edit the details of the item.
                    ShoppingItemEditor(item = item, onEditComplete = { editedName, editedQuantity ->

                        // Once the editing is complete, update the list of items to set isEditing to false for all
                        // items.
                        // This ensures that only one item can be edited at a time.
                        sItems = sItems.map { it.copy(isEditing = false) }

                        // Find the item that was just edited
                        val editedItem = sItems.find { it.id == item.id }

                        // If the item is found, update its name and quantity with the new values
                        editedItem?.let {
                            it.name = editedName
                            it.quantity = editedQuantity
                        }
                    })
                } else {

                    // If the item is not being edited, display the ShoppingListItem Composable.
                    // This Composable is used to display the details of the item.
                    ShoppingListItem(item = item, onEditClick = {

                        // When the edit button is clicked, update the list of items to set isEditing to true for the
                        // current item.
                        // This will cause the ShoppingItemEditor Composable to be displayed for this item.
                        sItems = sItems.map { it.copy(isEditing = it.id == item.id) }
                    }, onDeleteClick = {

                        // When the delete button is clicked, remove the current item from the list of items.
                        sItems = sItems - item
                    })
                }
            }
        }
    }

    // UI Element: AlertDialog (Used for adding a new item to the list)
    if (showDialog) {
        AlertDialog(onDismissRequest = { showDialog = false }, confirmButton = {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp), horizontalArrangement = Arrangement.SpaceBetween
            ) {
                // UI Element: Button (Used for closing the dialog without adding a new item)
                Button(onClick = { showDialog = false }) {
                    Text(text = "Cancel")
                }
                // UI Element: Button (Used for adding a new item to the list)
                Button(onClick = {
                    if (itemName.isNotBlank()) {
                        val newItem = ShoppingItem(
                            id = sItems.size + 1, name = itemName, quantity = itemQuantity.toInt()
                        )
                        sItems = sItems + newItem
                        showDialog = false
                        itemName = ""
                        itemQuantity = ""
                    }
                }) {
                    Text(text = "Done")
                }
            }
        }, title = { Text(text = "Add Shopping Item") }, text = {
            Column {
                // UI Element: OutlinedTextField (Used for entering the name of the new item)
                OutlinedTextField(
                    value = itemName,
                    onValueChange = { itemName = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            8.dp
                        )
                )
                // UI Element: OutlinedTextField (Used for entering the quantity of the new item)
                OutlinedTextField(
                    value = itemQuantity,
                    onValueChange = { itemQuantity = it },
                    singleLine = true,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(
                            8.dp
                        )
                )
            }
        })
    }
}

@Composable
fun ShoppingItemEditor(
    item: ShoppingItem, onEditComplete: (String, Int) -> Unit
) {
    var editedName by remember {
        mutableStateOf(item.name)
    }
    var editedQuantity by remember {
        mutableStateOf(item.quantity.toString())
    }
    var isEditing by remember {
        mutableStateOf(item.isEditing)
    }
    // UI Element: Row
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // UI Element: BasicTextField (Used for editing the name of the item)
            BasicTextField(
                value = editedName,
                onValueChange = { editedName = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
                    .background(Color.White)
            )
            // UI Element: BasicTextField (Used for editing the quantity of the item)
            BasicTextField(
                value = editedQuantity,
                onValueChange = { editedQuantity = it },
                singleLine = true,
                modifier = Modifier
                    .wrapContentSize()
                    .padding(8.dp)
                    .background(Color.White)
            )
            // UI Element: Button (Used for saving the edited item)
            Button(onClick = {
                isEditing = false
                onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1)
            }) {
                Text(text = "Save")
            }
        }
    }
}

@Composable
fun ShoppingListItem(
    item: ShoppingItem, onEditClick: () -> Unit, onDeleteClick: () -> Unit
) {
    // UI Element: Row
    Row(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .border(
                border = BorderStroke(
                    3.dp, Color(0xFF3990CA)
                ), shape = RoundedCornerShape(20)
            ), horizontalArrangement = Arrangement.SpaceBetween
    ) {
        // UI Element: Text (Used for displaying the name of the item)
        Text(text = item.name, modifier = Modifier.padding(13.dp, 20.dp))
        // UI Element: Text (Used for displaying the quantity of the item)
        Text(text = "Qty: ${item.quantity}", modifier = Modifier.padding(13.dp, 20.dp))
        Row(modifier = Modifier.padding(8.dp)) {
            // UI Element: IconButton (Used for editing the item)
            IconButton(onClick = onEditClick) {
                Icon(imageVector = Icons.Default.Edit, contentDescription = null)
            }
            // UI Element: IconButton (Used for deleting the item)
            IconButton(onClick = onDeleteClick) {
                Icon(imageVector = Icons.Default.Delete, contentDescription = null)
            }
        }
    }
}