package com.example.appmuabandocu.feature_home.ui
import ProductViewModel
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appmuabandocu.R
import com.example.appmuabandocu.ui.theme.Blue_text
import coil.compose.AsyncImage
import com.example.appmuabandocu.data.Product


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
                CategoryItem("Đồ điện tử", R.drawable.ic_phone)
                CategoryItem("Xe máy", R.drawable.ic_xemay)
                CategoryItem("Thời trang", R.drawable.ic_aoo)
                CategoryItem("Đồ gia dụng", R.drawable.ic_noicom)
                CategoryItem("Khác", R.drawable.ic_condit)
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
                            ProductItem(product = product, navController = navController) // truyền navController vào
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun CategoryItem(title: String, imageRes: Int) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(50.dp) // Kích thước khung tròn
                .clip(CircleShape)
                .background(Color.White)
                .border(1.dp, Color.Black, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.size(30.dp), // 👈 Thu nhỏ hình ảnh bên trong
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 10.sp,
            textAlign = TextAlign.Center
        )
    }
}


@Composable
fun ProductItem(
    product: Product,
    onContactClick: () -> Unit = {},
    navController: NavController
) {
    Column(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth()
            .clip(RoundedCornerShape(8.dp))
            .background(Color.White)
            .border(1.dp, Color.LightGray, RoundedCornerShape(8.dp))
    ) {
        // Hình ảnh sản phẩm
        AsyncImage(
            model = product.imageUrl.replace("http://", "https://"),
            contentDescription = "Hình sản phẩm",
            contentScale = ContentScale.Crop,
            modifier = Modifier
                .fillMaxWidth()
                .height(150.dp)
                .clip(RoundedCornerShape(topStart = 8.dp, topEnd = 8.dp)),
            placeholder = painterResource(id = R.drawable.ic_noicom),
            error = painterResource(id = R.drawable.ic_xemay)
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Tên và giá
        Column(modifier = Modifier.padding(horizontal = 12.dp)) {
            Text(
                text = product.productName,
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                maxLines = 2
            )

            Text(
                text = "Giá: ${formatPrice(product.price)}",
                color = Color(0xFF4CAF50),
                fontSize = 14.sp,
                modifier = Modifier.padding(top = 4.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Nút liên hệ
        Button(
            onClick = { navController.navigate("product_detail/${product.id}") },
            modifier = Modifier
                .fillMaxWidth()
                .height(42.dp)
                .padding(horizontal = 12.dp),
            shape = RoundedCornerShape(6.dp),
            colors = ButtonDefaults.buttonColors(containerColor = Blue_text)
        ) {
            Text(
                text = "Xem ngay",
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = Color.White
            )
        }


        Spacer(modifier = Modifier.height(8.dp))
    }
}

fun formatPrice(price: String): String {
    return try {
        val number = price.toDouble()
        val formatter = java.text.NumberFormat.getInstance(java.util.Locale("vi", "VN"))
        "${formatter.format(number)}₫"
    } catch (e: NumberFormatException) {
        price // Trả nguyên chuỗi nếu không phải số, ví dụ "Thỏa thuận"
    }
}