package com.example.appmuabandocu.feature_profile

import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.Icon
import androidx.compose.material.OutlinedTextField
import androidx.compose.material.Text
import androidx.compose.material.TextFieldDefaults
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.appmuabandocu.R
import com.example.appmuabandocu.model.User
import com.example.appmuabandocu.viewmodel.ProfileViewModel
import com.example.appmuabandocu.ui.theme.Blue_text
import com.google.firebase.auth.FirebaseAuth

@Composable
fun ProfileDetailScreen(
    navController: NavController,
    auth: FirebaseAuth,
    profileViewModel: ProfileViewModel = viewModel()
) {
    val context = LocalContext.current
    val user = auth.currentUser

    val isLoading by profileViewModel.isLoading.collectAsState()
    val avatarUrl by profileViewModel.avatarUrl.collectAsState()
    val message by profileViewModel.message.collectAsState()
    val userData by profileViewModel.userData.collectAsState()

    var isEditing by remember { mutableStateOf(false) }

    var fullName by remember(userData) { mutableStateOf(userData?.fullName ?: user?.displayName ?: "") }
    var phoneNumber by remember(userData) { mutableStateOf(userData?.phoneNumber ?: "") }
    var address by remember(userData) { mutableStateOf(userData?.address ?: "") }

    LaunchedEffect(message) {
        message?.let {
            Toast.makeText(context, it, Toast.LENGTH_SHORT).show()
            profileViewModel.clearMessage()
        }
    }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? ->
        if (uri != null) {
            profileViewModel.uploadProfileImage(context, uri)
        }
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(16.dp))
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ) {
            Icon(
                painter = painterResource(id = R.drawable.back_left),
                contentDescription = "Back",
                modifier = Modifier
                    .padding(end = 8.dp)
                    .clickable { navController.popBackStack() },
                tint = Blue_text
            )
            Text(
                "Thông tin cá nhân",
                color = Blue_text,
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 16.dp, bottom = 16.dp),
                textAlign = TextAlign.Center
            )
        }

        Spacer(modifier = Modifier.padding(16.dp))

            Column(
                modifier = Modifier.fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Profile picture with edit button
                Box(
                    modifier = Modifier
                        .size(120.dp)
                        .padding(8.dp),
                    contentAlignment = Alignment.Center
                ) {
                    if (avatarUrl.isNotEmpty()) {
                        AsyncImage(
                            model = avatarUrl.replace("http://", "https://"),
                            contentDescription = "Ảnh đại diện",
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .border(2.dp, Blue_text, CircleShape),
                            contentScale = ContentScale.Crop
                        )
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxSize()
                                .clip(CircleShape)
                                .background(Color.LightGray)
                                .border(2.dp, Blue_text, CircleShape)
                        )
                    }

                    // Edit icon
                    Box(
                        modifier = Modifier
                            .align(Alignment.BottomEnd)
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Blue_text)
                            .border(1.dp, Color.White, CircleShape)
                            .clickable { imagePicker.launch("image/*") },
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Edit,
                            contentDescription = "Edit Profile Picture",
                            tint = Color.White,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(24.dp))

                // Show loading indicator when uploading
                if (isLoading) {
                    CircularProgressIndicator(
                        color = Blue_text,
                        modifier = Modifier.size(48.dp)
                    )
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        "Đang xử lý...",
                        color = Blue_text
                    )
                } else {
                    // User details
                    Column(
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = fullName,
                            onValueChange = { if (isEditing) fullName = it },
                            label = { Text("Tên người dùng", color = Blue_text) },
                            readOnly = !isEditing,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Blue_text,
                                unfocusedBorderColor = Blue_text,
                                cursorColor = if (isEditing) Blue_text else Color.Transparent,
                                textColor = Blue_text
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = userData?.email ?: user?.email ?: "",
                            onValueChange = { },
                            label = { Text("Email", color = Blue_text) },
                            readOnly = true,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Blue_text,
                                unfocusedBorderColor = Blue_text,
                                cursorColor = Color.Transparent,
                                textColor = Blue_text
                            )
                        )

                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = phoneNumber,
                            onValueChange = { if (isEditing) phoneNumber = it },
                            label = { Text("Số điện thoại", color = Blue_text) },
                            readOnly = !isEditing,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Blue_text,
                                unfocusedBorderColor = Blue_text,
                                cursorColor = Color.Transparent,
                                textColor = Blue_text
                            )
                        )
                        Spacer(modifier = Modifier.height(8.dp))

                        OutlinedTextField(
                            modifier = Modifier.fillMaxWidth(),
                            value = address,
                            onValueChange = { if (isEditing) address = it },
                            label = { Text("Địa chỉ", color = Blue_text) },
                            readOnly = !isEditing,
                            colors = TextFieldDefaults.outlinedTextFieldColors(
                                focusedBorderColor = Blue_text,
                                unfocusedBorderColor = Blue_text,
                                cursorColor = Color.Transparent,
                                textColor = Blue_text
                            )
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        // Edit button
                        Button(
                            onClick = {
                                if (isEditing) {
                                    // Save changes
                                    val updatedUser = userData?.copy(
                                        fullName = fullName,
                                        phoneNumber = phoneNumber,
                                        address = address
                                    ) ?: User(
                                        email = user?.email ?: "",
                                        fullName = fullName,
                                        phoneNumber = phoneNumber,
                                        address = address
                                    )
                                    profileViewModel.updateUserProfile(updatedUser)
                                }
                                isEditing = !isEditing
                            },
                            modifier = Modifier.fillMaxWidth(),
                            colors = androidx.compose.material3.ButtonDefaults.buttonColors(
                                containerColor = Blue_text
                            )
                        ) {
                            Text(if (isEditing) "Lưu thông tin" else "Chỉnh sửa thông tin")
                        }
                    }
                }
            }

    }
}