package com.example.appmuabandocu.feature_home.ui

import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Divider
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
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.appmuabandocu.R
import com.example.appmuabandocu.ui.theme.Blue_text


@Preview(showBackground = true)
@Composable
fun HomeScreen(modifier: Modifier = Modifier) {
    Box(
        modifier = modifier
            .fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.bg2_screen),
            contentDescription = "Home Screen",
            contentScale = ContentScale.FillBounds // cat anh vua khung hinh
        )
        Column(
            modifier = Modifier
                .fillMaxSize()
        ){
            Spacer(modifier = Modifier.height(30.dp))
            Row(
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.Bottom,
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_app),
                    contentDescription = "Logo",
                    modifier = Modifier.size(80.dp)
                )
                Text(
                    text = "Thanh ly nhanh",
                    fontSize = 22.sp,
                    fontWeight = Bold,
                    color = Blue_text,
                    modifier = Modifier.padding(bottom = 16.dp)

                )
            }
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),

                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                IconButton(
                    modifier = Modifier
                        .size(50.dp)
                        .border(2.dp , Color.White, CircleShape),
                    onClick = {
                        // Xử lý khi nút được nhấn
                    }

                ){
                    Image(
                        painter = painterResource(id = R.drawable.huynguyen),
                        contentDescription = "Profile",
                    )
                }
                var SearchText = remember { mutableStateOf("") }
                OutlinedTextField(
                    value = "searchText",
                    onValueChange = {
                        SearchText.value = it
                    },
                    placeholder = { Text("Bạn muốn mua gì ?") },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        //focusedContainerColor = Color.White,
                        focusedIndicatorColor = Blue_text,
                        unfocusedIndicatorColor = Blue_text,
                        unfocusedContainerColor = Color.White,
                    )

                )
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(
                    modifier = Modifier
                        .border(2.dp , Color.White, RoundedCornerShape(10.dp)),
                    onClick = {
                        // Xử lý khi nút được nhấn
                    }
                ){
                    Image(
                        Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(40.dp)
                    )
                }

            }
            Spacer(modifier = Modifier.height(20.dp))
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                CategoryItem("Thiết bị \n điện tử", R.drawable.device)
                CategoryItem("Xe máy", R.drawable.moto)
                CategoryItem("Thời trang", R.drawable.apparel)
                CategoryItem("Đồ gia dụng", R.drawable.chair)
                CategoryItem("Khác", R.drawable.oder)
            }
            Spacer(modifier = Modifier.height(10.dp))
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                items(10) { // giả sử có 10 bài đăng
                    ProductItem(
                        userName = "HuyNguyen",
                        location = "Thảo Điền - TP.HCM",
                        productName = "Camera mẫu mới giá rẻ",
                        price = "2.3tr",
                        imageUrl = "https://picsum.photos/seed/picsum/200/300"
                    )
                }
            }
        }

    }
}

@Composable
fun CategoryItem(title: String, iconRes: Int,) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally) {
        IconButton(
            onClick = { /* TODO: Xử lý khi bấm */ },
            modifier = Modifier
                .border(
                    width = 1.dp,
                    color = Color.Black,

                )
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(title, fontSize = 10.sp,
            textAlign = TextAlign.Center,
        )
    }
}