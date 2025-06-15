package com.example.appmuabandocu.feature_profile

import android.util.Log
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.appmuabandocu.R
import com.example.appmuabandocu.model.Product
import com.example.appmuabandocu.model.User
import com.example.appmuabandocu.repository.ProductRepository
import com.example.appmuabandocu.repository.UserRepository
import com.example.appmuabandocu.utils.NetworkConnectivityObserver
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.example.appmuabandocu.core.navigation.model.Screen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileUserScreen(
    navController: NavController,
    userId: String
) {
    // Log userId để debug
    Log.d("ProfileUserScreen", "ProfileUserScreen được gọi với userId: '$userId'")

    // Get context safely
    val context = LocalContext.current

    // Create dependencies
    val firestore = FirebaseFirestore.getInstance()
    val auth = FirebaseAuth.getInstance()
    val networkObserver = remember { NetworkConnectivityObserver(context) }

    // Create repositories
    val userRepository = remember { UserRepository(firestore, auth, networkObserver) }
    val productRepository = remember { ProductRepository(firestore, networkObserver) }

    val viewModel = remember {
        ProfileUserViewModel(userRepository, productRepository)
    }

    val userState by viewModel.user.collectAsState()
    val productsState by viewModel.userProducts.collectAsState()
    val isLoading by viewModel.isLoading.collectAsState()
    val errorMessage by viewModel.errorMessage.collectAsState()

    LaunchedEffect(userId) {
        viewModel.loadUserProfile(userId)
        viewModel.loadUserProducts(userId)
    }

    val displayProducts = productsState.filter { it.status == "available" || it.status == "sold" }



    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text("Thông tin người bán") },
                navigationIcon = {
                    IconButton(onClick = { navController.popBackStack() }) {
                        Icon(
                            imageVector = Icons.Default.ArrowBack,
                            contentDescription = "Quay lại"
                        )
                    }
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            if (isLoading) {
                CircularProgressIndicator(
                    modifier = Modifier.align(Alignment.Center)
                )
            } else {
                // Show error if any
                errorMessage?.let { error ->
                    Column(
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center
                    ) {
                        Text(
                            text = error,
                            color = Color.Red,
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(16.dp))
                        Button(onClick = {
                            viewModel.clearError()
                            viewModel.loadUserProfile(userId)
                            viewModel.loadUserProducts(userId)
                        }) {
                            Text("Thử lại")
                        }
                    }
                } ?: run {
                    // Show user data if no error
                    LazyColumn(
                        modifier = Modifier.fillMaxSize(),
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        item {
                            UserInfoSection(userState)
                        }

                        item {
                            Text(
                                text = "Sản phẩm đã đăng",
                                modifier = Modifier.padding(horizontal = 16.dp),
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }

                        item {
                            if (displayProducts.isEmpty()) {
                                Text(
                                    text = "Không có sản phẩm nào",
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(16.dp),
                                    textAlign = TextAlign.Center,
                                    color = Color.Gray
                                )
                            } else {
                                ProductGrid(products = displayProducts, navController = navController)
                            }
                        }
                    }
                }
            }
        }
    }
}

@Composable
fun UserInfoSection(user: User?) {
    if (user == null) return

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Avatar
        Box(
            modifier = Modifier
                .size(100.dp)
                .clip(CircleShape)
                .background(Color.LightGray)
        ) {
            if (user.avatarUrl.isNotEmpty()) {
                AsyncImage(
                    model = user.avatarUrl.replace("http://", "https://"),
                    contentDescription = user.fullName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop,
                )
                Image(
                    painter = rememberAsyncImagePainter(user.avatarUrl),
                    contentDescription = "Avatar",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )
            } else {
                Text(
                    text = user.fullName.firstOrNull()?.toString() ?: "?",
                    modifier = Modifier
                        .align(Alignment.Center),
                    fontSize = 32.sp,
                    fontWeight = FontWeight.Bold
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Name
        Text(
            text = user.fullName,
            fontSize = 20.sp,
            fontWeight = FontWeight.Bold
        )

        Spacer(modifier = Modifier.height(8.dp))

        // Contact info
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Icon(
                imageVector = Icons.Default.Call,
                contentDescription = "Phone",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = user.phoneNumber ?: "Không có số điện thoại",
                fontSize = 14.sp,
                color = if (user.phoneNumber != null) Color.Black else Color.Gray
            )
        }

        Spacer(modifier = Modifier.height(8.dp))

        // Address
        val fullAddress = buildString {
            user.ward?.let {
                if (isNotEmpty()) append(", ")
                append(it)
            }
            user.district?.let {
                if (isNotEmpty()) append(", ")
                append(it)
            }
            user.province?.let {
                if (isNotEmpty()) append(", ")
                append(it)
            }
        }

        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.Top
        ) {
            Icon(
                imageVector = Icons.Default.LocationOn,
                contentDescription = "Address",
                tint = MaterialTheme.colorScheme.primary,
                modifier = Modifier.size(20.dp)
            )
            Spacer(modifier = Modifier.width(8.dp))
            Text(
                text = if (fullAddress.isNotEmpty()) fullAddress else "Không có địa chỉ",
                fontSize = 14.sp,
                color = if (fullAddress.isNotEmpty()) Color.Black else Color.Gray
            )
        }
    }
}

@Composable
fun ProductGrid(products: List<Product>, navController: NavController) {
    // Calculate the height needed for the grid based on number of products
    val itemCount = products.size
    val rowCount = (itemCount + 1) / 2 // Calculate rows needed (2 items per row, rounded up)
    val height = (rowCount * 220).dp // Each item is about 220dp tall

    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        contentPadding = PaddingValues(8.dp),
        modifier = Modifier
            .fillMaxWidth()
            .height(height) // Use fixed height instead of letting parent decide
    ) {
        items(products) { product ->
            ProductCard(product, navController)
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProductCard(product: Product, navController: NavController) {
    Card(
        modifier = Modifier
            .padding(8.dp)
            .fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        onClick = {
            navController.navigate("product_detail/${product.id}")
        }
    ) {
        Column(modifier = Modifier.fillMaxWidth()) {
            // Product Image
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(120.dp)
                    .background(Color.LightGray)
            ) {
                AsyncImage(
                    model = product.imageUrl.replace("http://", "https://"),
                    contentDescription = product.productName,
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Crop
                )


                if (product.status == "sold") {
                    Box(
                        modifier = Modifier
                            .fillMaxSize()
                            .background(Color.Black.copy(alpha = 0.5f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Đã bán",
                            color = Color.White,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            // Product Info
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp)
            ) {
                Text(
                    text = product.productName,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    fontWeight = FontWeight.Medium
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = "${product.price} ₫",
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = product.address,
                    fontSize = 12.sp,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    color = Color.Gray
                )
            }
        }
    }
}
