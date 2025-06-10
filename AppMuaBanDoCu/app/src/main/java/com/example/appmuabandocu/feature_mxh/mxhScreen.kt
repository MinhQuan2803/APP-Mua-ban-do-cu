package com.example.appmuabandocu.feature_mxh

import android.content.Intent
import android.util.Log
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
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.ExperimentalMaterialApi
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.pullrefresh.PullRefreshIndicator
import androidx.compose.material.pullrefresh.pullRefresh
import androidx.compose.material.pullrefresh.rememberPullRefreshState
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.appmuabandocu.R
import com.example.appmuabandocu.core.navigation.BottomNavBar
import com.example.appmuabandocu.model.Product
import com.example.appmuabandocu.ui.theme.Blue_text
import com.example.appmuabandocu.viewmodel.ProductViewModel
import com.example.appmuabandocu.viewmodel.SearchProductViewModel
import java.text.NumberFormat
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale




@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MxhScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    productViewModel: ProductViewModel = viewModel(),
    searchViewModel: SearchProductViewModel = viewModel(),
){
    val products = productViewModel.getVisibleProducts()
    val searchResults by searchViewModel.searchResults.collectAsState<List<Product>>()
    var query by remember { mutableStateOf("") }
    val isRefreshing by productViewModel.isRefreshing.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { productViewModel.refreshProducts() }
    )

    val sortedSearchResults = searchResults.sortedByDescending { it.timestamp }

    // Log để kiểm tra danh sách sản phẩm
    LaunchedEffect(products) {
        Log.d("mxhScreen", "Sản phẩm: ${products.size} sản phẩm")
    }
    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->

    Box(
        modifier = Modifier.padding(paddingValues)
    ){
        Column(
        ){
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Blue_text)
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 10.dp, vertical = 10.dp),
                    horizontalAlignment = Alignment.Start // Canh trái
                ) {
                    Text(
                        text = "Thanh lý nhanh",
                        fontSize = 22.sp,
                        fontWeight = FontWeight.Bold,
                        color = Color.White
                    )
                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            value = query,
                            onValueChange = {
                                query = it
                                searchViewModel.searchProducts(it)
                            },
                            shape = RoundedCornerShape(12.dp),
                            placeholder = {
                                Text(
                                    text = "Bạn muốn mua gì?",
                                    fontSize = 14.sp,
                                    maxLines = 1,
                                    overflow = TextOverflow.Ellipsis
                                )
                            },
                            colors = TextFieldDefaults.colors(
                                focusedTextColor = Color.Black,
                                focusedIndicatorColor = Blue_text,
                                unfocusedIndicatorColor = Blue_text,
                                unfocusedContainerColor = Color(0xFFF5F5F5),
                                focusedContainerColor = Color(0xFFF5F5F5)
                            ),
                            modifier = Modifier
                                .weight(1f)
                                .height(60.dp)
                        )

                        Spacer(modifier = Modifier.width(12.dp))

                        IconButton(
                            onClick = { /* TODO: Tìm kiếm */ },
                            modifier = Modifier
                                .size(52.dp)
                                .background(Color.White, RoundedCornerShape(12.dp))
                                .border(1.dp, Blue_text, RoundedCornerShape(12.dp))
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Search",
                                tint = Blue_text,
                                modifier = Modifier.size(28.dp)
                            )
                        }
                    }
                }
            }

            Box(
                Modifier
                    .pullRefresh(pullRefreshState)
                    .fillMaxWidth()
            ){
                LazyColumn {
                    items(sortedSearchResults.size) { index ->
                        ProductItemMXH(
                            product1 = sortedSearchResults[index],
                            navController = navController,
                            toggleProductVisibility = {
                                productViewModel.toggleProductVisibility(
                                    sortedSearchResults[index].id
                                )
                            }
                        )
                    }
                }
                PullRefreshIndicator(
                    refreshing = isRefreshing,
                    state = pullRefreshState,
                    modifier = Modifier.align(Alignment.TopCenter)
                )
            }

        }
    }

    Spacer(modifier = Modifier.height(200.dp))
}
}
@Composable
fun ProductItemMXH(
    product1: Product,
    navController: NavController,
    toggleProductVisibility: () -> Unit
) {
    val formattedTime = remember(product1.timestamp) {
        val date = Date(product1.timestamp)
        val formatter = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
        formatter.format(date)
    }
    val phoneNumber = product1.numberUser
    val context = LocalContext.current


    Card(
        modifier = Modifier
            .fillMaxWidth()
            .background(Color.White)
            .padding(bottom = 8.dp),
        colors = CardDefaults.cardColors(containerColor = Color.White),
        elevation = CardDefaults.cardElevation(defaultElevation = 0.dp),
        onClick = { navController.navigate("product_detail/${product1.id}") },
    ) {
        Divider(
            color = Blue_text,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Column(modifier = Modifier.padding(12.dp)) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                AsyncImage(
                    model = product1.userAvatar.replace("http://", "https://"),
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape),
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(
                        buildAnnotatedString {
                            withStyle(style = SpanStyle(fontWeight = FontWeight.Bold, color = Color.Black)) {
                                append(product1.userName)
                            }
                            withStyle(style = SpanStyle(color = Color.Black)) {
                                append(" đã đăng một mặt hàng")
                            }
                        },
                        fontSize = 14.sp
                    )
                    Row {
                        Text(text = formattedTime, fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = product1.provinceFB, fontSize = 12.sp, color = Color.Gray)
                    }
                }
                Spacer(modifier = Modifier.weight(1f))


                Icon(
                    imageVector = Icons.Default.MoreVert,
                    contentDescription = "More"
                )
            }


            Spacer(modifier = Modifier.height(8.dp))


            // Product Image
            AsyncImage(
                model = product1.imageUrl.replace("http://", "https://"),
                contentDescription = null,
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .fillMaxWidth()
                    .wrapContentHeight()
                    .clip(RoundedCornerShape(8.dp)),
                placeholder = painterResource(id = R.drawable.placeholders_product), // Thêm ảnh placeholder
                error = painterResource(id = R.drawable.error), // Thêm ảnh khi có lỗi
            )


            Spacer(modifier = Modifier.height(8.dp))


            // Product Info
            Text(text = "Tên sản phẩm: ${product1.productName}", fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.Black)
            Text(text = "Giá bán: ${formatPrice(product1.price)}", color = Color.Black, fontSize = 15.sp)


            Spacer(modifier = Modifier.height(8.dp))


            // Contact Button
            TextButton(
                onClick = {  val intent = Intent(Intent.ACTION_DIAL).apply {
                    data = "tel:$phoneNumber".toUri()
                }
                    context.startActivity(intent) },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = Blue_text
                )
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = "Liên hệ ngay",
                    color = Blue_text,
                    fontWeight = FontWeight.Bold,
                    fontSize = 16.sp
                )
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