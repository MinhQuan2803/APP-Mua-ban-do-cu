package com.example.appmuabandocu.feature_profile.ui

import android.widget.Toast
import androidx.compose.foundation.Image
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
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.appmuabandocu.R
import com.example.appmuabandocu.ui.theme.Blue_text
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
@Composable
fun ProfileScreen(auth: FirebaseAuth, onSignIn: () -> Unit, onSignOut: () -> Unit,navController: NavController) {
    val context = LocalContext.current
    val user = auth.currentUser

    Column(
        modifier = Modifier
            .fillMaxSize()
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

        if (user == null) {
            navController.navigate("login_screen")
        } else {
            // Hiển thị giao diện khi đã đăng nhập
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Image(
                    painter = rememberAsyncImagePainter(user.photoUrl),
                    contentDescription = "Ảnh đại diện",
                    modifier = Modifier
                        .size(80.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )

                Spacer(modifier = Modifier.width(12.dp))

                Column {
                    Text(
                        text = user.displayName ?: "Người dùng",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold
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
                ProfileOption("Thông tin cá nhân") { navController.navigate("profile_detail")}
                ProfileOption("Quản lý mặt hàng") { navController.navigate("profile_manage_screen") }
                ProfileOption("Liên hệ") { Toast.makeText(context, "Mở liên hệ", Toast.LENGTH_SHORT).show() }
                ProfileOption("Trợ giúp") { Toast.makeText(context, "Mở trợ giúp", Toast.LENGTH_SHORT).show() }

                Spacer(modifier = Modifier.height(16.dp))

                Button(
                    onClick = onSignOut,
                    colors = ButtonDefaults.buttonColors(containerColor = colorResource(id = R.color.mauchinh))
                ) {
                    Text(text = "Đăng xuất", color = Color.Black)
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
