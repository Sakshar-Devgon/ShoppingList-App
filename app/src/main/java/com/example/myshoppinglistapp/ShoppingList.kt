package com.example.myshoppinglistapp

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
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.myshoppinglistapp.ui.theme.MyShoppingListAppTheme
import java.nio.ByteOrder


data class ShoppingItem(
    val id: Int,
    var name: String,
    var quantity: Int,
    var isEditing: Boolean = false
)

@Composable
fun ShoppingListApp() {
    var sItems by remember { mutableStateOf(listOf<ShoppingItem>()) } // holds the list of Shopping Item
    var showDialog by remember { mutableStateOf(false) }
    var itemName by remember { mutableStateOf("") }
    var itemQuantity by remember { mutableStateOf("") }

    Scaffold(
        modifier = Modifier.fillMaxSize()
    ) { innerPadding ->
        // Pass the innerPadding to the content inside
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding), // Apply padding from Scaffold
            verticalArrangement = Arrangement.Center,
        ) {
            Button(
                onClick = { showDialog = true },
                modifier = Modifier.align(Alignment.CenterHorizontally)
            ) {
                Text("Add Item")
            }
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp),
            ) {
                // Add your LazyColumn items here
                items(sItems){
                    item ->
                    if(item.isEditing){
                        ShoppingItemEditor(item = item , onEditComplete ={
                            editedName , editedQuantity ->
                            sItems = sItems.map { it.copy(isEditing = false) }
                            val editedItem = sItems.find { it.id == item.id }
                            editedItem?.let {
                                it.name = editedName
                                it.quantity = editedQuantity
                            }
                        } )
                    }else{
                        ShoppingListItem(item = item , onEditClick = {
                            // finding out which item we are editing and changing is "isEditing boolean" to true
                            sItems = sItems.map { it.copy(isEditing = it.id == item.id) }
                        }, onDeleteClick = {
                            sItems = sItems - item
                        })


                    }
                }

            }
        }
    }
    if(showDialog){
      AlertDialog(onDismissRequest = {/*TODO*/},
          confirmButton = {
              Row (modifier = Modifier.fillMaxWidth().padding(8.dp),
                  horizontalArrangement = Arrangement.SpaceBetween) {
                  Button(onClick = {
                      if(itemName.isNotBlank()){
                          val newItem = ShoppingItem(
                              id = sItems.size + 1,
                              name = itemName,
                              quantity = itemQuantity.toInt()
                          )
                          sItems = sItems + newItem
                          showDialog = false
                      }
                  }) {
                      Text("Add")
                  }
                  Button(onClick = {showDialog = false}) {
                      Text("Cancel")
                  }

              }
          },
          title = { Text("Add Shopping Item") },
          text = {
              Column {
                  OutlinedTextField(
                      value = itemName,
                      onValueChange = {itemName = it },
                      singleLine = true,
                      modifier = Modifier.fillMaxWidth().padding(8.dp)
                  )

                  OutlinedTextField(
                      value = itemQuantity,
                      onValueChange = {itemQuantity = it },
                      singleLine = true,
                      modifier = Modifier.fillMaxWidth().padding(8.dp)
                  )
              }
          }
      )

    }

}

@Composable
fun ShoppingItemEditor(item: ShoppingItem, onEditComplete: (String, Int) -> Unit) {
    var editedName by remember { mutableStateOf(item.name) }
    var editedQuantity by remember { mutableStateOf(item.quantity.toString()) } // Added for quantity
    var isEditing by remember { mutableStateOf(item.isEditing) }

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceEvenly
    ) {
        Column {
            // BasicTextField for editing name
            BasicTextField(
                value = editedName,
                onValueChange = { editedName = it },
                singleLine = true,
                modifier = Modifier.wrapContentSize().padding(8.dp)
            )

            // BasicTextField for editing quantity
            BasicTextField(  // This is the new field for quantity
                value = editedQuantity,
                onValueChange = { editedQuantity = it },
                singleLine = true,
                modifier = Modifier.wrapContentSize().padding(8.dp)
            )
        }

        Button(
            onClick = {
                isEditing = false
                onEditComplete(editedName, editedQuantity.toIntOrNull() ?: 1) // Pass both name and quantity
            }
        ) {
            Text("Save")
        }
    }
}



@Composable
fun ShoppingListItem(
    item: ShoppingItem, // object of shopping item data class!
    onEditClick: () -> Unit,
    onDeleteClick: () -> Unit,
){
    Row (modifier = Modifier.padding(8.dp).fillMaxWidth().border(
        border = BorderStroke(2.dp, Color(0XFF018786)),
        shape = RoundedCornerShape(20)
        ),
        horizontalArrangement = Arrangement.SpaceBetween  // this gave equal space of edit or delete fucntions and item name&qyt
    ){
        Text(text = item.name, modifier = Modifier.padding(8.dp))
        Text(text = "Qty: ${item.quantity}", modifier = Modifier.padding(8.dp))
        Row (modifier = Modifier.padding(8.dp)){

            IconButton(onClick = onEditClick){
                Icon(imageVector = Icons.Default.Edit , contentDescription = null)
            }

            IconButton(onClick = onDeleteClick){
                Icon(imageVector = Icons.Default.Delete , contentDescription = null)
            }
        }
    }

}


//@Preview(showBackground = true)
//@Composable
//fun ShopAppPreview() {
//    MyShoppingListAppTheme {
//        ShoppingListApp()
//    }
//}
