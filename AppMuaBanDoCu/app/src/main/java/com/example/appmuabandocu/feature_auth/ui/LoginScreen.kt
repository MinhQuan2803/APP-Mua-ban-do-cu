package com.example.appmuabandocu.feature_auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appmuabandocu.R
import com.example.appmuabandocu.ui.theme.Blue_text
import androidx.navigation.compose.rememberNavController
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.runtime.*
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import com.example.appmuabandocu.feature_auth.SignInState
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    state: SignInState,
    onSignInClick: () -> Unit,
    navController: NavController,
    ) {

    val context = LocalContext.current

    LaunchedEffect(key1 = state.signInError) {
        state.signInError?.let { error ->
            Toast.makeText(
                context,
                error,
                Toast.LENGTH_LONG
            ).show()
        }
    }

    Box(
        modifier = modifier.fillMaxSize()
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.bg_screen),
            contentDescription = "Splash Screen",
            contentScale = ContentScale.FillBounds
        )
        Box(
            modifier = Modifier.fillMaxSize(),
            contentAlignment = Alignment.Center
        ) {
            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                Image(
                    painter = painterResource(id = R.drawable.logo_app),
                    contentDescription = "Logo",
                )
                Text(
                    text = "Đăng nhập",
                    fontSize = 38.sp,
                    fontWeight = Bold,
                    color = Blue_text,
                )

//                OutlinedTextField(
//                    value = email,
//                    onValueChange = { email = it },
//                    label = { Text(text = "Email") },
//                    colors = TextFieldDefaults.colors(
//                        focusedTextColor = Color.Black,
//                        focusedIndicatorColor = Blue_text,
//                        unfocusedIndicatorColor = Blue_text,
//                        unfocusedContainerColor = Color.White,
//                        unfocusedTextColor = Color.Black
//                    )
//                )

                Spacer(modifier = Modifier.height(8.dp))

//                OutlinedTextField(
//                    value = password,
//                    onValueChange = { password = it },
//                    label = { Text(text = "Password") },
//                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
//                    trailingIcon = {
//                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
//                            Icon(
//                                imageVector = if (passwordVisible)
//                                    Icons.Filled.VisibilityOff
//                                else
//                                    Icons.Filled.Visibility,
//                                contentDescription = if (passwordVisible) "Ẩn mật khẩu" else "Hiện mật khẩu"
//                            )
//                        }
//                    },
//                    colors = TextFieldDefaults.colors(
//                        focusedTextColor = Color.Black,
//                        focusedIndicatorColor = Blue_text,
//                        unfocusedIndicatorColor = Blue_text,
//                        unfocusedContainerColor = Color.White,
//                        unfocusedTextColor = Color.Black
//                    )
//                )

//                TextButton(
//                    onClick = { navController.navigate("homeNav") },
//                    modifier = Modifier.align(Alignment.End)
//                ) {
//                    Text(text = "Quên mật khẩu", fontSize = 16.sp, color = Blue_text)
//                }
//
//                Button(
//                    onClick = {
//                        auth.signInWithEmailAndPassword(email, password)
//                            .addOnCompleteListener { task ->
//                                if (task.isSuccessful) {
//                                    Toast.makeText(context, "Đăng nhập thành công!", Toast.LENGTH_SHORT).show()
//                                    navController.navigate("homeNav") // Điều hướng sau khi đăng nhập thành công
//                                } else {
//                                    Toast.makeText(context, "Đăng nhập thất bại!", Toast.LENGTH_SHORT).show()
//                                }
//                            }
//                    },
//                    modifier = Modifier
//                        .align(Alignment.CenterHorizontally)
//                        .width(250.dp),
//                    colors = ButtonDefaults.buttonColors(
//                        containerColor = Blue_text,
//                        contentColor = Color.White
//                    )
//                ) {
//                    Text(text = "Đăng nhập")
//                }

//                TextButton(onClick = { navController.navigate("register_main_screen") }) {
//                    Text(text = "Chưa có tài khoản ? Đăng ký ngay")
//                }


//                Text(text = "OR", fontSize = 14.sp, fontWeight = FontWeight.Bold)

                Spacer(modifier = Modifier.height(16.dp))

                // Social Media Icons
                Button(
                    onClick = onSignInClick,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .width(300.dp)
                        .height(60.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color(0xFFD5EDFF),
                        contentColor = Color.Black
                    ),
                    shape = RoundedCornerShape(8.dp)

                ) {
                    Image(
                        modifier = Modifier.size(24.dp),
                        painter = painterResource(id = R.drawable.ic_google),
                        contentDescription = "Google Icon"
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = "Sign in with Google")
                }
            }
        }
    }
}
