package com.example.appmuabandocu.feature_profile.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.appmuabandocu.R
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileDetailScreen(modifier: Modifier = Modifier, navController: NavController, auth: FirebaseAuth) {
    val context = LocalContext.current
    val user = auth.currentUser

    Column (
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally


    ){
        Spacer(modifier = Modifier.padding(16.dp))
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            Icon(
                painter = painterResource(id = R.drawable.back_left),
                contentDescription = "Back",
                modifier = Modifier.padding(end = 8.dp)
                    .clickable { navController.popBackStack() },
            )
            Text(
                "Thông tin cá nhân",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 16.dp),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))
        if (user == null) {
            navController.navigate("login_screen")
        } else {
                Column (
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ){
                    Image(
                        painter = rememberAsyncImagePainter(user.photoUrl),
                        contentDescription = "Ảnh đại diện",
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape),
                        contentScale = ContentScale.Crop
                    )
                    Column (
                        modifier = Modifier.fillMaxWidth()
                    ){
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = user.displayName ?: "",
                            onValueChange = { },
                            label = { Text("Tên người dùng") },
                            readOnly = true

                        )
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = user.email ?: "",
                            onValueChange = { },
                            label = { Text("Email") },
                            readOnly = true
                        )
                    }
                }
            }
        }
}
