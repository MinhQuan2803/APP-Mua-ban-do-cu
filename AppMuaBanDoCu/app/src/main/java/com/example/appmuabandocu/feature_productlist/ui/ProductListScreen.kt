package com.example.appmuabandocu.feature_productlist.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appmuabandocu.R
import com.example.appmuabandocu.ui.theme.Blue_text
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.material3.ButtonDefaults
import androidx.compose.ui.draw.clip


@Preview(showBackground = true)
@Composable
fun ProductListScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
    ) {
        Column(){
            Box(
                modifier = Modifier.fillMaxWidth()
                    .height(140.dp)
                    .background(Blue_text),

                contentAlignment = Alignment.Center
            ){
                Spacer(modifier = Modifier.height(30.dp))
                Box {
                    Row (

                    ){
                        var SearchText = remember { mutableStateOf("") }
                        OutlinedTextField(
                            value = "searchText",
                            onValueChange = {
                                SearchText.value = it
                            },
                            shape = RoundedCornerShape(10.dp),
                            placeholder = { Text("Bạn muốn mua gì ?") },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                //focusedContainerColor = Color.White,
                                focusedIndicatorColor = Blue_text,
                                unfocusedIndicatorColor = Blue_text,
                                unfocusedContainerColor = Color(0xFFEEEEEE),
                            ),
                            modifier = Modifier.fillMaxWidth(0.7f)
                                .height(50.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        IconButton(
                            modifier = Modifier
                                .border(2.dp , Color.White, RoundedCornerShape(10.dp)),
                            onClick = { }
                        ){
                            Image(
                                Icons.Default.Search,
                                contentDescription = "Search",
                                modifier = Modifier.size(40.dp)
                            )
                        }
                    }
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                val categories = listOf("TV", "Laptop", "Điện thoại", "camera cũ", "Tủ lạnh")
                Row(modifier = Modifier.padding(8.dp)) {
                    categories.forEach { category ->
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .background(Color.LightGray, RoundedCornerShape(8.dp))
                                .padding(8.dp)
                        ) {
                            Text(category, fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ){
                val products = List(10) { "Camera" to "500.000 đ" }

                LazyVerticalGrid(
                    columns = GridCells.Fixed(2), // ✅ Chia 2 cột
                    modifier = Modifier.padding(8.dp)
                ) {
                    items(products) { (name, price) ->
                        ProductItem(name, price)
                    }
                }
            }

        }
    }
}

@Composable
fun ProductItem(name: String, price: String) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        Image(
            painter = painterResource(id = R.drawable.pd_camera),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(8.dp))
        )
        Row {
            Column {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = price,
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = {},
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier
                    .padding(2.dp)
                    .width(100.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Blue_text
                )

            ) {
                Text("Liên hệ")
            }
        }

    }
}

