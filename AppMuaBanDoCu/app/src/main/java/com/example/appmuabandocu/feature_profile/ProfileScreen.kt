package com.example.appmuabandocu.feature_profile

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
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
import com.example.appmuabandocu.core.navigation.BottomNavBar
import com.example.appmuabandocu.core.navigation.model.Screen
import com.example.appmuabandocu.viewmodel.ProfileViewModel
import com.example.appmuabandocu.ui.theme.Blue_text
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileScreen(
    auth: FirebaseAuth,
    onSignOut: () -> Unit,
    navController: NavController,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val user = auth.currentUser
    val avatarUrl by profileViewModel.avatarUrl.collectAsState()

    LaunchedEffect(key1 = user) {
        if (user == null) {
            navController.navigate(Screen.Login.route) {
                popUpTo(Screen.Profile.route) { inclusive = true }
            }
        }
    }

    Scaffold(
        bottomBar = { BottomNavBar(navController = navController) }
    ) { paddingValues ->

        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.padding(16.dp))
            Text(
                text = "Thông tin của bạn ",
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

            if (user != null) {
                // Hiển thị giao diện khi đã đăng nhập
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp)
                ) {
                    if (avatarUrl.isNotEmpty()) {
                        AsyncImage(
                            model = avatarUrl.replace("http://", "https://"),
                            contentDescription = "Ảnh đại diện",
                            modifier = Modifier
                                .size(80.dp)
                                .clip(CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .clip(CircleShape)
                                .background(Color.LightGray)
                                .size(80.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column {
                        Text(
                            text = user.displayName ?: "Người dùng",
                            fontSize = 18.sp,
                            fontWeight = Bold
                        )
                        Text(
                            text = user.email ?: "",
                            fontSize = 14.sp,
                            color = Color.Gray
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Column(
                    modifier = Modifier.fillMaxWidth()
                ) {
                    ProfileOption("Thông tin cá nhân") { navController.navigate(Screen.ProfileDetail.route) }
                    ProfileOption("Quản lý bài viết") { navController.navigate(Screen.ManageProduct.route) }
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Button(
                            onClick = {
                                onSignOut()
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.mauchinh)),
                            modifier = Modifier.height(50.dp)
                                .width(150.dp)
                        ) {
                            Text(text = "Đăng xuất", color = Color.Black)
                        }

                    }
                }

            } else {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                    CircularProgressIndicator(color = Blue_text)
                }
            }


        }
    }
}
@Composable
fun ProfileOption(title: String, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp, horizontal = 16.dp),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = title, fontSize = 16.sp, fontWeight = FontWeight.Medium)
        Icon(painterResource(id = R.drawable.ic_next), contentDescription = "Next")
    }
}
