package com.example.appmuabandocu.feature_home.ui
import ProductViewModel
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
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
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
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
import com.example.appmuabandocu.viewmodel.SearchProductViewModel




@Composable
fun HomeScreen(modifier: Modifier = Modifier,
               navController: NavController,
               viewModel: ProductViewModel = viewModel(),
               searchViewModel: SearchProductViewModel = viewModel()


) {

    val products = viewModel.getVisibleProducts()

    val searchResults by searchViewModel.searchResults.collectAsState<List<Product>>()
    var query by remember { mutableStateOf("") }

    val focusManager = LocalFocusManager.current

    var selectedCategory by remember { mutableStateOf<String?>(null) }
    val filteredProducts = products.filter {
        selectedCategory == null || it.category == selectedCategory
    }

    // Kết quả cuối cùng cần hiển thị (ưu tiên kết quả tìm kiếm nếu có)
    val displayProducts = if (query.isNotBlank()) searchResults else filteredProducts

    val sortedProducts = displayProducts.sortedByDescending { it.timestamp }

    // Log để kiểm tra danh sách sản phẩm
    LaunchedEffect(products) {
        Log.d("HomeScreen", "Sản phẩm: ${products.size} sản phẩm")
    }

    Box(
        modifier = modifier.fillMaxSize()
            .clickable(
                indication = null,
                interactionSource = remember { MutableInteractionSource() }
            ) {
                focusManager.clearFocus() // thoát focus khi click ra ngoài
            },
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
                OutlinedTextField(
                    value = query,
                    onValueChange = {
                        query = it
                        searchViewModel.searchProducts(it)
                    },
                    placeholder = { Text("Bạn muốn mua gì ?", fontSize = 12.sp) },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
                        focusedIndicatorColor = Blue_text,
                        unfocusedIndicatorColor = Blue_text,
                        unfocusedContainerColor = Color.White,
                    ),
                    modifier = Modifier.height(56.dp)
                        .weight(1f),


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
                CategoryItem("Đồ điện tử", R.drawable.ic_phone, selectedCategory == "Thiết bị điện tử") {
                    selectedCategory = if (selectedCategory == "Thiết bị điện tử") null else "Thiết bị điện tử"
                }
                CategoryItem("Xe máy", R.drawable.ic_xemay, selectedCategory == "Xe cộ") {
                    selectedCategory = if (selectedCategory == "Xe cộ") null else "Xe cộ"
                }
                CategoryItem("Thời trang", R.drawable.ic_aoo, selectedCategory == "Quần áo") {
                    selectedCategory = if (selectedCategory == "Quần áo") null else "Quần áo"
                }
                CategoryItem("Đồ gia dụng", R.drawable.ic_noicom, selectedCategory == "Đồ gia dụng") {
                    selectedCategory = if (selectedCategory == "Đồ gia dụng") null else "Đồ gia dụng"
                }
                CategoryItem("Khác", R.drawable.ic_condit, selectedCategory == "Khác") {
                    selectedCategory = if (selectedCategory == "Khác") null else "Khác"
                }
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
                        items(sortedProducts) { product ->
                            ProductItem(
                                product = product,
                                navController = navController,
                                toggleProductVisibility = { viewModel.toggleProductVisibility(product.id) }
                            )
                        }
                    }
                }
            }
        }
    }
}


@Composable
fun CategoryItem(
    title: String,
    imageRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier.clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(50.dp)
                .clip(CircleShape)
                .background( Color.White)
                .border(2.dp, if (isSelected) Blue_text else Color.Black, CircleShape),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = null,
                modifier = Modifier.size(30.dp),
                contentScale = ContentScale.Fit
            )
        }

        Spacer(modifier = Modifier.height(8.dp))
        Text(
            text = title,
            fontSize = 10.sp,
            textAlign = TextAlign.Center,
            color = if (isSelected) Blue_text else Color.Black,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
        )
    }
}




@Composable
fun ProductItem(
    product: Product,
    onContactClick: () -> Unit = {},
    navController: NavController,
    toggleProductVisibility: () -> Unit
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
                maxLines = 2,
                color = Color.Black
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
