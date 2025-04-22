package com.example.appmuabandocu.feature_mxh.ui


import ProductViewModel
import android.content.Intent
import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
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
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.net.toUri
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.appmuabandocu.R
import com.example.appmuabandocu.data.Product
import com.example.appmuabandocu.ui.theme.Blue_text
import com.example.appmuabandocu.viewmodel.SearchProductViewModel
import com.google.firebase.auth.FirebaseAuth
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale




@OptIn(ExperimentalMaterialApi::class)
@Composable
fun MxhScreen(
    modifier: Modifier = Modifier,
    navController: NavController,
    viewModel: ProductViewModel = viewModel(),
    searchViewModel: SearchProductViewModel = viewModel()
){
    val products = viewModel.getVisibleProducts()


    val searchResults by searchViewModel.searchResults.collectAsState<List<Product>>()
    var query by remember { mutableStateOf("") }

    val isRefreshing by viewModel.isRefreshing.collectAsState()
    val pullRefreshState = rememberPullRefreshState(
        refreshing = isRefreshing,
        onRefresh = { viewModel.refreshProducts() }
    )


    // Log để kiểm tra danh sách sản phẩm
    LaunchedEffect(products) {
        Log.d("mxhScreen", "Sản phẩm: ${products.size} sản phẩm")
    }

    Box(
        modifier = modifier.fillMaxSize()
    ){
        Column(
        ){
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
                        OutlinedTextField(
                            value = query,
                            onValueChange = {
                                query = it
                                searchViewModel.searchProducts(it)
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
//            Box(
//                modifier = Modifier.fillMaxWidth(),
//                contentAlignment = Alignment.Center
//            ){
//                val categories = listOf("TV", "Laptop", "Điện thoại", "camera cũ", "Tủ lạnh")
//                Row(modifier = Modifier.padding(8.dp)) {
//                    categories.forEach { category ->
//                        Box(
//                            modifier = Modifier
//                                .padding(4.dp)
//                                .background(Color.LightGray, RoundedCornerShape(8.dp))
//                                .padding(8.dp)
//                        ) {
//                            Text(category, fontWeight = FontWeight.Bold)
//                        }
//                    }
//                }
//            }
            Box(
                Modifier
                    .pullRefresh(pullRefreshState)
                    .fillMaxWidth()
            ){
                LazyColumn(
                ) {
                    items(searchResults.size) { index ->
                        ProductItemMXH(
                            product1 = searchResults[index],
                            navController = navController,
                            toggleProductVisibility = {
                                viewModel.toggleProductVisibility(
                                    searchResults[index].id
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
                Image(
                    painter = rememberAsyncImagePainter(product1.userAvatar),
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
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
                placeholder = painterResource(id = R.drawable.ic_noicom), // Thêm ảnh placeholder
                error = painterResource(id = R.drawable.ic_condit), // Thêm ảnh khi có lỗi
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
        val formatter = java.text.NumberFormat.getInstance(java.util.Locale("vi", "VN"))
        "${formatter.format(number)}₫"
    } catch (e: NumberFormatException) {
        price // Trả nguyên chuỗi nếu không phải số, ví dụ "Thỏa thuận"
    }
}