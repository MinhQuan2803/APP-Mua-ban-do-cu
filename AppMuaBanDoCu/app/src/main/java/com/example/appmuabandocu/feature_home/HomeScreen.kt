package com.example.appmuabandocu.feature_home

import android.util.Log
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.expandVertically
import androidx.compose.animation.shrinkVertically
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Notifications
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
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
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.ColorFilter
import androidx.compose.ui.graphics.SolidColor
import androidx.compose.ui.input.nestedscroll.NestedScrollConnection
import androidx.compose.ui.input.nestedscroll.NestedScrollSource
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.example.appmuabandocu.R
import com.example.appmuabandocu.ui.theme.Blue_text
import coil.compose.AsyncImage
import com.example.appmuabandocu.core.navigation.BottomNavBar
import com.example.appmuabandocu.model.Product
import com.example.appmuabandocu.viewmodel.FavoriteViewModel
import com.example.appmuabandocu.viewmodel.ProductViewModel
import com.example.appmuabandocu.viewmodel.SearchProductViewModel
import java.text.NumberFormat
import java.util.Locale


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    navController: NavController,
    productViewModel: ProductViewModel = viewModel(),
    searchViewModel: SearchProductViewModel = viewModel(),
) {

    val products = productViewModel.getVisibleProducts()

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

    // Thêm ScrollState để theo dõi trạng thái cuộn
    val scrollState = rememberScrollState()

    // Biến để kiểm soát hiển thị phần đầu
    var isHeaderVisible by remember { mutableStateOf(true) }

    // Lưu trữ vị trí cuộn trước đó để xác định hướng cuộn
    var previousScrollOffset by remember { mutableStateOf(0) }

    // NestedScrollConnection để phát hiện hướng cuộn
    val nestedScrollConnection = remember {
        object : NestedScrollConnection {
            override fun onPreScroll(available: Offset, source: NestedScrollSource): Offset {
                // Xác định hướng cuộn
                val delta = available.y

                // Cập nhật trạng thái hiển thị header dựa trên hướng cuộn
                // Cuộn xuống (delta < 0) -> ẩn header
                // Cuộn lên (delta > 0) -> hiện header
                if (delta < -10) { // Ngưỡng để tránh thay đổi liên tục
                    isHeaderVisible = false
                } else if (delta > 10) {
                    isHeaderVisible = true
                }

                return Offset.Zero
            }
        }
    }

    // Log để kiểm tra danh sách sản phẩm
    LaunchedEffect(products) {
        Log.d("HomeScreen", "Sản phẩm: ${products.size} sản phẩm")
    }
    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Thanh Lý Nhanh",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = Blue_text
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                ),
                actions = {
                    IconButton(onClick = { /* Xử lý thông báo */ }) {
                        Icon(
                            imageVector = Icons.Default.Notifications,
                            contentDescription = "Notifications",
                            tint = Blue_text
                        )
                    }
                    IconButton(onClick = { /* Xử lý tin nhắn */ }) {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = "Messages",
                            tint = Blue_text
                        )
                    }
                }
            )
        },
        bottomBar = { BottomNavBar(navController = navController) }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .padding(
                    bottom = innerPadding.calculateBottomPadding(),
                    top = innerPadding.calculateTopPadding(),
                    start = innerPadding.calculateStartPadding(LocalLayoutDirection.current),
                    end = innerPadding.calculateEndPadding(LocalLayoutDirection.current)
                )
                .fillMaxSize()
                .clickable(
                    indication = null,
                    interactionSource = remember { MutableInteractionSource() }
                ) {
                    focusManager.clearFocus() // thoát focus khi click ra ngoài
                }
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color(0xFFF8FCFF))
            )
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(horizontal = 16.dp)
            ) {
                AnimatedVisibility(
                    visible = isHeaderVisible,
                    enter = slideInVertically() + expandVertically(),
                    exit = slideOutVertically() + shrinkVertically()
                ) {
                    Column {
                        Spacer(modifier = Modifier.height(16.dp))

                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(60.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = Color.White)
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(horizontal = 8.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Search,
                                    contentDescription = "Search",
                                    tint = Blue_text,
                                    modifier = Modifier.padding(start = 8.dp)
                                )

                                BasicTextField(
                                    value = query,
                                    onValueChange = {
                                        query = it
                                        searchViewModel.searchProducts(it)
                                    },
                                    modifier = Modifier
                                        .weight(1f)
                                        .padding(horizontal = 12.dp),
                                    textStyle = TextStyle(
                                        fontSize = 16.sp,
                                        color = Color.Black
                                    ),
                                    decorationBox = { innerTextField ->
                                        Box(
                                            modifier = Modifier.fillMaxWidth(),
                                            contentAlignment = Alignment.CenterStart
                                        ) {
                                            if (query.isEmpty()) {
                                                Text(
                                                    text = "Bạn muốn mua gì?",
                                                    fontSize = 16.sp,
                                                    color = Color.Gray
                                                )
                                            }
                                            innerTextField()
                                        }
                                    },
                                    singleLine = true,
                                    cursorBrush = SolidColor(Blue_text)
                                )

                                IconButton(
                                    onClick = { /* Xử lý lọc */ },
                                    modifier = Modifier.size(40.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.FilterList,
                                        contentDescription = "Filter",
                                        tint = Blue_text
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(24.dp))

                        Text(
                            text = "Danh mục",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Black,
                            modifier = Modifier.padding(bottom = 12.dp)
                        )

                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(16.dp),
                            contentPadding = PaddingValues(horizontal = 4.dp)
                        ) {
                            item {
                                CategoryItemImproved(
                                    title = "Đồ điện tử",
                                    imageRes = R.drawable.ic_phone,
                                    isSelected = selectedCategory == "Thiết bị điện tử",
                                    onClick = {
                                        selectedCategory =
                                            if (selectedCategory == "Thiết bị điện tử") null else "Thiết bị điện tử"
                                    }
                                )
                            }
                            item {
                                CategoryItemImproved(
                                    title = "Xe máy",
                                    imageRes = R.drawable.ic_xemay,
                                    isSelected = selectedCategory == "Xe cộ",
                                    onClick = {
                                        selectedCategory =
                                            if (selectedCategory == "Xe cộ") null else "Xe cộ"
                                    }
                                )
                            }
                            item {
                                CategoryItemImproved(
                                    title = "Thời trang",
                                    imageRes = R.drawable.ic_aoo,
                                    isSelected = selectedCategory == "Quần áo",
                                    onClick = {
                                        selectedCategory =
                                            if (selectedCategory == "Quần áo") null else "Quần áo"
                                    }
                                )
                            }
                            item {
                                CategoryItemImproved(
                                    title = "Đồ gia dụng",
                                    imageRes = R.drawable.ic_noicom,
                                    isSelected = selectedCategory == "Đồ gia dụng",
                                    onClick = {
                                        selectedCategory =
                                            if (selectedCategory == "Đồ gia dụng") null else "Đồ gia dụng"
                                    }
                                )
                            }
                            item {
                                CategoryItemImproved(
                                    title = "Khác",
                                    imageRes = R.drawable.ic_condit,
                                    isSelected = selectedCategory == "Khác",
                                    onClick = {
                                        selectedCategory =
                                            if (selectedCategory == "Khác") null else "Khác"
                                    }
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }
                // Danh sách sản phẩm
                if (products.isEmpty()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(1f),
                        contentAlignment = Alignment.Center
                    ) {
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            CircularProgressIndicator(color = Blue_text)
                            Spacer(modifier = Modifier.height(16.dp))
                            Text(
                                text = "Đang tải sản phẩm...",
                                color = Color.Gray
                            )
                        }
                    }
                } else {
                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2), // Chia 2 cột
                        contentPadding = PaddingValues(bottom = 16.dp),
                        horizontalArrangement = Arrangement.spacedBy(12.dp),
                        verticalArrangement = Arrangement.spacedBy(16.dp),
                        modifier = Modifier
                            .weight(1f)
                            .nestedScroll(nestedScrollConnection)
                    ) {
                        items(sortedProducts) { product ->
                            ProductItem(
                                product = product,
                                navController = navController,
                                toggleProductVisibility = {
                                    productViewModel.toggleProductVisibility(
                                        product.id
                                    )
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}
@Composable
fun CategoryItemImproved(
    title: String,
    imageRes: Int,
    isSelected: Boolean,
    onClick: () -> Unit
) {
    Column(
        modifier = Modifier
            .width(80.dp)
            .clickable { onClick() },
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Box(
            modifier = Modifier
                .size(60.dp)
                .background(
                    color = if (isSelected) Blue_text.copy(alpha = 0.1f) else Color.White,
                    shape = RoundedCornerShape(12.dp)
                )
                .border(
                    width = if (isSelected) 2.dp else 1.dp,
                    color = if (isSelected) Blue_text else Color.LightGray,
                    shape = RoundedCornerShape(12.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Image(
                painter = painterResource(id = imageRes),
                contentDescription = title,
                modifier = Modifier.size(32.dp),
                contentScale = ContentScale.Fit,
                colorFilter = if (isSelected) ColorFilter.tint(Blue_text) else null
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        Text(
            text = title,
            fontSize = 12.sp,
            textAlign = TextAlign.Center,
            color = if (isSelected) Blue_text else Color.Black,
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            maxLines = 1,
            overflow = TextOverflow.Ellipsis
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

    val favoriteViewModel: FavoriteViewModel = viewModel()
    val favoriteIds = favoriteViewModel.favoriteProductIds.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth(),
        onClick = {
            navController.navigate("product_detail/${product.id}")
        },
        shape = RoundedCornerShape(12.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Column() {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
            ) {
                // Hình ảnh sản phẩm
                AsyncImage(
                    model = product.imageUrl.replace("http://", "https://"),
                    contentDescription = product.productName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                    placeholder = painterResource(id = R.drawable.placeholders_product),
                    error = painterResource(id = R.drawable.error)
                )
                // Badge trạng thái
                if (product.status == "Đã bán") {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f))
                    )
                    Text(
                        text = "Đã bán",
                        color = Color.White,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier
                            .align(Alignment.Center)
                            .background(
                                color = Color.Red.copy(alpha = 0.7f),
                                shape = RoundedCornerShape(4.dp)
                            )
                            .padding(horizontal = 8.dp, vertical = 4.dp)
                    )
                }

                // Nút yêu thích
                IconButton(
                    onClick = { favoriteViewModel.toggleFavorite(product.id) },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .size(36.dp)
                        .padding(4.dp)
                        .offset((-10).dp, 10.dp)
                        .background(
                            color = Color.White.copy(alpha = 0.7f),
                            shape = CircleShape
                        )
                ) {
                    Icon(
                        imageVector = if (favoriteIds.value.contains(product.id)) Icons.Filled.Favorite else Icons.Filled.FavoriteBorder,
                        contentDescription = "Favorite",
                        tint = if (favoriteIds.value.contains(product.id)) Color.Red else Color.Gray,
                        modifier = Modifier.size(20.dp)
                    )
                }
            }
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(12.dp)
            ) {
                Text(
                    text = product.productName,
                    fontSize = 14.sp,
                    fontWeight = FontWeight.Medium,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Black
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${NumberFormat.getNumberInstance(Locale("vi", "VN")).format(product.price.toDouble())} VNĐ",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Bold,
                    color = Blue_text
                )
                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = "Location",
                        tint = Color.Gray,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = product.address,
                        fontSize = 12.sp,
                        color = Color.Gray,
                        maxLines = 1,
                        overflow = TextOverflow.Ellipsis,
                        modifier = Modifier.weight(1f)
                    )
                }
            }
        }
    }
}
fun formatPrice(price: String): String {
    return try {
        val number = price.toDouble()
        val formatter = NumberFormat.getInstance(Locale("vi", "VN"))
        "${formatter.format(number)}₫"
    } catch (e: NumberFormatException) {
        price // Trả nguyên chuỗi nếu không phải số, ví dụ "Thỏa thuận"
    }
}
