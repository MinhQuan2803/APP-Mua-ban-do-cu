package com.example.appmuabandocu.feature_mxh.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.example.appmuabandocu.ui.theme.Blue_text
import com.example.appmuabandocu.feature_home.ui.ProductItem


@Preview(showBackground = true)
@Composable
fun MxhScreen(modifier: Modifier = Modifier) {
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
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(10) {
                    ProductItem(
                        userName = "HuyNguyen",
                        location = "Thảo Điền - TP.HCM",
                        productName = "Camera mẫu mới giá rẻ",
                        price = "2.3tr",
                        imageUrl = "https://picsum.photos/seed/picsum/200/300",
                        time = "1 h"
                    )
                }
            }

        }
    }
}


