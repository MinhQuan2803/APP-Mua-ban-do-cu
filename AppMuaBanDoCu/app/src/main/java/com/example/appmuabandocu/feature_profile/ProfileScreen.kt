package com.example.appmuabandocu.feature_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ExitToApp
import androidx.compose.material.icons.automirrored.filled.Help
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.ExitToApp
import androidx.compose.material.icons.filled.Help
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.ShoppingBag
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.appmuabandocu.R
import com.example.appmuabandocu.component.FeatureInDevelopmentDialog
import com.example.appmuabandocu.core.navigation.BottomNavBar
import com.example.appmuabandocu.core.navigation.model.Screen
import com.example.appmuabandocu.viewmodel.ProfileViewModel
import com.example.appmuabandocu.ui.theme.Blue_text
import com.example.appmuabandocu.ui.theme.orange
import com.google.firebase.auth.FirebaseAuth

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    auth: FirebaseAuth,
    onSignOut: () -> Unit,
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val user = auth.currentUser
    val avatarUrl by profileViewModel.avatarUrl.collectAsState()

    var showDevelopmentDialog by remember { mutableStateOf(false) }

    if (showDevelopmentDialog) {
        FeatureInDevelopmentDialog(
            onDismiss = { showDevelopmentDialog = false },
            featureName = "Tính năng" // Tùy chỉnh tên tính năng
        )
    }

    LaunchedEffect(key1 = user) {
        if (user == null) {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Profile.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Tài khoản",
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold
                        )
                    )
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = orange,
                    titleContentColor = Blue_text
                ),
                actions = {
                    IconButton(onClick = { /* Mở cài đặt */ }) {
                        Icon(
                            imageVector = Icons.Default.Settings,
                            contentDescription = "Cài đặt",
                            tint = Blue_text
                        )
                    }
                }
            )
        },
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->
        if (user == null) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues),
                contentAlignment = Alignment.Center
            ) {
                CircularProgressIndicator(color = Blue_text)
            }
        } else {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .background(Color(0xFFF8FCFF))
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                ) {
                    // Phần thông tin người dùng
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(16.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(100.dp)
                                    .padding(4.dp)
                                    .border(
                                        width = 2.dp,
                                        color = Blue_text,
                                        shape = CircleShape
                                    )
                                    .padding(4.dp)
                                    .clip(CircleShape)
                                    .background(Color.LightGray),
                                contentAlignment = Alignment.Center
                            ) {
                                if (avatarUrl.isNotEmpty()) {
                                    AsyncImage(
                                        model = avatarUrl.replace("http://", "https://"),
                                        contentDescription = "Ảnh đại diện",
                                        modifier = Modifier.fillMaxSize(),
                                        contentScale = ContentScale.Crop
                                    )
                                } else {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = "Người dùng",
                                        tint = Color.White,
                                        modifier = Modifier.size(48.dp)
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Text(
                                text = user.displayName ?: "Người dùng",
                                style = MaterialTheme.typography.titleLarge.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                color = Color.Black

                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = user.email ?: "",
                                style = MaterialTheme.typography.bodyMedium,
                                color = Color.Gray
                            )

                            Spacer(modifier = Modifier.height(16.dp))

                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceEvenly
                            ) {
                                ProfileStat(count = "12", title = "Đã đăng")
                                Divider(
                                    modifier = Modifier
                                        .height(40.dp)
                                        .width(1.dp),
                                    color = Color.LightGray
                                )
                                ProfileStat(count = "5", title = "Đã bán")
                                Divider(
                                    modifier = Modifier
                                        .height(40.dp)
                                        .width(1.dp),
                                    color = Color.LightGray
                                )
                                ProfileStat(count = "8", title = "Yêu thích")
                            }

                            Spacer(modifier = Modifier.height(16.dp))

                            Button(
                                onClick = { navController.navigate(Screen.ProfileDetail.route) },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp),
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Blue_text.copy(alpha = 0.1f),
                                    contentColor = Blue_text
                                ),
                                elevation = null
                            ) {
                                Text(
                                    text = "Chỉnh sửa thông tin",
                                    style = MaterialTheme.typography.bodyMedium.copy(
                                        fontWeight = FontWeight.Medium
                                    )
                                )
                            }
                        }
                    }

                    // Phần tùy chọn
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = "Tài khoản",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                color = Color.Black
                            )

                            ProfileOptionImproved(
                                icon = Icons.Default.Person,
                                title = "Thông tin cá nhân",
                                subtitle = "Xem và chỉnh sửa thông tin cá nhân",
                            ) {
                                navController.navigate(Screen.ProfileDetail.route)
                            }

                            ProfileOptionImproved(
                                icon = Icons.Default.ShoppingBag,
                                title = "Quản lý bài viết",
                                subtitle = "Xem và quản lý các sản phẩm đã đăng"
                            ) {
                                navController.navigate(Screen.ManageProduct.route)
                            }

                            ProfileOptionImproved(
                                icon = Icons.Default.History,
                                title = "Lịch sử giao dịch",
                                subtitle = "Xem các giao dịch mua bán đã thực hiện",
                                onClick = {
                                    showDevelopmentDialog = true
                                }
                            )
                            ProfileOptionImproved(
                                icon = Icons.Default.Star,
                                title = "Đánh giá",
                                subtitle = "Xem đánh giá từ người dùng khác",
                                onClick = {
                                    showDevelopmentDialog = true
                                }
                            )
                        }
                    }

                    // Phần hỗ trợ
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(16.dp),
                        shape = RoundedCornerShape(16.dp),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color.White)
                    ) {
                        Column(
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = "Hỗ trợ",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold
                                ),
                                modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp),
                                color = Color.Black
                            )
                        }

                        ProfileOptionImproved(
                            icon = Icons.Default.Help,
                            title = "Trung tâm trợ giúp",
                            subtitle = "Câu hỏi thường gặp và hướng dẫn",
                            onClick = {
                                showDevelopmentDialog = true
                            }
                        )

                        ProfileOptionImproved(
                            icon = Icons.Default.Info,
                            title = "Về ứng dụng",
                            subtitle = "Phiên bản, điều khoản và chính sách",
                            onClick = {
                                showDevelopmentDialog = true
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        Button(
                            onClick = { onSignOut() },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .padding(horizontal = 16.dp),
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(
                                containerColor = Color(0xFFFF5252),
                                contentColor = Color.White
                            )
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.ExitToApp,
                                contentDescription = "Đăng xuất",
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "Đăng xuất",
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.Medium
                                )
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))
            }
        }
    }
}

@Composable
fun ProfileStat(count: String, title: String) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = count,
            style = MaterialTheme.typography.titleLarge.copy(
                fontWeight = FontWeight.Bold,
                color = Blue_text
            )
        )
        Text(
            text = title,
            style = MaterialTheme.typography.bodySmall,
            color = Color.Black
        )
    }
}

@Composable
fun ProfileOptionImproved(
    icon: ImageVector,
    title: String,
    subtitle: String,
    onClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 16.dp, vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(40.dp)
                .background(
                    color = Blue_text.copy(alpha = 0.1f),
                    shape = RoundedCornerShape(8.dp)
                ),
            contentAlignment = Alignment.Center
        ) {
            Icon(
                imageVector = icon,
                contentDescription = title,
                tint = Blue_text,
                modifier = Modifier.size(24.dp)
            )
        }

        Spacer(modifier = Modifier.width(16.dp))

        Column(
            modifier = Modifier.weight(1f)
        ) {
            Text(
                text = title,
                style = MaterialTheme.typography.bodyLarge.copy(
                    fontWeight = FontWeight.Medium
                ),
                color = Color.Black

            )
            Text(
                text = subtitle,
                style = MaterialTheme.typography.bodySmall,
                color = Color.Gray
            )
        }

        Icon(
            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
            contentDescription = "Tiếp theo",
            tint = Color.Gray
        )
    }
}