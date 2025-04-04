package com.example.appmuabandocu.feature_home.ui

import ProductViewModel
import android.util.Log
import android.widget.ImageButton
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.RectangleShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberImagePainter
import com.example.appmuabandocu.R
import com.example.appmuabandocu.ui.theme.Blue_text


@Composable
fun HomeScreen(modifier: Modifier = Modifier, navController: NavController, viewModel: ProductViewModel = viewModel()) {
    val products = viewModel.productList

    // Log để kiểm tra danh sách sản phẩm
    LaunchedEffect(products) {
        Log.d("HomeScreen", "Sản phẩm: ${products.size} sản phẩm")
    }
    Box(
        modifier = modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Image(
            modifier = Modifier.fillMaxSize().matchParentSize(),
            painter = painterResource(id = R.drawable.bg2_screen),
            contentDescription = "Home Screen",
            contentScale = ContentScale.FillBounds // cắt ảnh vừa khung hình
        )
        Column(
            modifier = Modifier.fillMaxSize()
        ) {
            // Tiêu đề trang
            Text(
                text = "Thanh lý nhanh",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Blue_text,
                modifier = Modifier.padding(10.dp).fillMaxWidth()
            )

            // Tìm kiếm
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(start = 10.dp, end = 10.dp),
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                var SearchText = remember { mutableStateOf("") }
                OutlinedTextField(
                    value = SearchText.value,
                    onValueChange = { SearchText.value = it },
                    placeholder = { Text("Bạn muốn mua gì ?", fontSize = 12.sp) },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedIndicatorColor = Blue_text,
                        unfocusedIndicatorColor = Blue_text,
                        unfocusedContainerColor = Color.White,
                    ),
                    modifier = Modifier.height(50.dp).weight(1f)
                )
                Spacer(modifier = Modifier.width(10.dp))
                IconButton(
                    modifier = Modifier
                        .border(2.dp, Color.White, RoundedCornerShape(10.dp)),
                    onClick = { /* Xử lý tìm kiếm */ }
                ) {
                    Icon(
                        imageVector = Icons.Default.Search,
                        contentDescription = "Search",
                        modifier = Modifier.size(40.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Danh mục sản phẩm
            Row(
                horizontalArrangement = Arrangement.SpaceEvenly,
                modifier = Modifier.fillMaxWidth()
            ) {
                CategoryItem("Đồ điện tử", R.drawable.device)
                CategoryItem("Xe máy", R.drawable.moto)
                CategoryItem("Thời trang", R.drawable.apparel)
                CategoryItem("Đồ gia dụng", R.drawable.chair)
                CategoryItem("Khác", R.drawable.oder)
            }

            Box(
                modifier = Modifier.fillMaxWidth(),
                contentAlignment = Alignment.Center
            ) {
                if (products.isEmpty()) {
                    Text("Không có sản phẩm nào")
                } else {
                    // Hiển thị danh sách sản phẩm
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2), // Chia 2 cột
                        modifier = Modifier.padding(8.dp)
                    ) {
                        items(products) { product ->
                            ProductItem(
                                name = product.productName,
                                price = product.price,
                                imageUrl = product.imageUrl // Giả sử bạn có link ảnh
                            )
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(title: String, iconRes: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        IconButton(
            onClick = { /* TODO: Xử lý khi bấm vào danh mục */ },
            modifier = Modifier.border(1.dp, Color.Black)
        ) {
            Icon(
                painter = painterResource(id = iconRes),
                contentDescription = null,
                modifier = Modifier.size(24.dp),
                tint = Color.Black
            )
        }
        Spacer(modifier = Modifier.height(8.dp))
        Text(title, fontSize = 10.sp, textAlign = TextAlign.Center)
    }
}
@Composable
fun ProductItem(name: String, price: String, imageUrl: String) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
    ) {
        // Hiển thị hình ảnh sản phẩm
        Image(
            painter = rememberImagePainter(imageUrl), // Lấy ảnh từ Firestore (sử dụng link URL)
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(8.dp))
        )

        // Hiển thị thông tin sản phẩm
        Row {
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(
                    text = name,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(top = 8.dp)
                )
                Text(
                    text = "Giá: $price ",
                    color = Color.Gray,
                    modifier = Modifier.padding(top = 4.dp)
                )
            }
            Spacer(modifier = Modifier.weight(1f))
            Button(
                onClick = { /* Liên hệ với người bán */ },
                shape = RoundedCornerShape(4.dp),
                modifier = Modifier.padding(2.dp).width(100.dp),
                colors = ButtonDefaults.buttonColors(containerColor = Blue_text)
            ) {
                Text("Liên hệ")
            }
        }
    }
}
