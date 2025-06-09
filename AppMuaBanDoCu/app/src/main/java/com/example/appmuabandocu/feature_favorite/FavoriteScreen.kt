package com.example.appmuabandocu.feature_favorite

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.appmuabandocu.R
import com.example.appmuabandocu.model.Product
import com.example.appmuabandocu.feature_home.formatPrice
import com.example.appmuabandocu.ui.theme.Blue_text
import com.example.appmuabandocu.viewmodel.FavoriteViewModel
import com.example.appmuabandocu.viewmodel.ProductViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun FavoriteScreen(
    navController: NavController,
    viewModel: ProductViewModel,
    favoriteViewModel: FavoriteViewModel
) {
    val auth = FirebaseAuth.getInstance()
    val user = auth.currentUser

    // Redirect to login if user is not logged in
    LaunchedEffect(key1 = user) {
        if (user == null) {
            navController.navigate("login_screen") {
                popUpTo("favorite_screen") { inclusive = true }
            }
        }
    }

    if (user != null) {
        // Only collect favorites if user is logged in
        val favoriteProductIds = favoriteViewModel.favoriteProductIds.collectAsState().value

        // Sửa lại cách lọc sản phẩm yêu thích
        val favoriteProducts = remember(viewModel.productList, favoriteProductIds) {
            viewModel.productList.filter { product ->
                favoriteProductIds.contains(product.id)
            }
        }

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(16.dp))
            Text(
                text = "Bài viết yêu thích",
                modifier = Modifier.padding(bottom = 16.dp),
                fontWeight = Bold,
                fontSize = 30.sp,
                color = Blue_text
            )
            Divider(
                color = Blue_text,
                thickness = 1.dp,
                modifier = Modifier.padding(bottom = 16.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))

            if (favoriteProducts.isEmpty()) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    shape = RoundedCornerShape(16.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                    colors = CardDefaults.cardColors(containerColor = Color.LightGray)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(24.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Image(
                            painter = painterResource(id = R.drawable.ic_empty_task),
                            contentDescription = "No Tasks",
                            modifier = Modifier.size(100.dp)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        Text(
                            text = "Không có sản phẩm yêu thích nào",
                            fontSize = 18.sp,
                            fontWeight = Bold,
                            color = Color.Black
                        )
                    }
                }
            } else {
                // Hiển thị sản phẩm yêu thích
                LazyColumn {
                    items(favoriteProducts.size) { index ->
                        ProductItem(
                            navController = navController,
                            product = favoriteProducts[index],
                            viewModel = favoriteViewModel
                        )
                    }
                }
            }
        }
    } else {
        // Hiển thị màn hình loading trong khi chuyển hướng
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            CircularProgressIndicator(color = Blue_text)
        }
    }
}

@Composable
fun ProductItem(
    product: Product,
    viewModel: FavoriteViewModel,
    navController: NavController
) {
    val favoriteIds = viewModel.favoriteProductIds.collectAsState()

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("product_detail/${product.id}")
            },
        shape = RoundedCornerShape(8.dp),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            AsyncImage(
                model = product.imageUrl.replace("http://", "https://"),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 16.dp),
                placeholder = painterResource(id = R.drawable.placeholders_product),
                error = painterResource(id = R.drawable.error),
            )

            Column(modifier = Modifier.weight(1f)) {
                Text(text = product.productName, style = MaterialTheme.typography.h6)
                Text(text = formatPrice(product.price), style = MaterialTheme.typography.body2)
                Text(text = "Địa chỉ: ${product.address}", style = MaterialTheme.typography.body2)
            }

            IconButton(
                onClick = { viewModel.toggleFavorite(product.id) },
            ) {
                Icon(
                    imageVector = if (favoriteIds.value.contains(product.id))
                        Icons.Default.Favorite else
                        Icons.Default.FavoriteBorder,
                    contentDescription = "Favorite"
                )
            }
        }
    }
}