package com.example.appmuabandocu.feature_auth.ui

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.TextField
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appmuabandocu.R
import com.example.appmuabandocu.ui.theme.Blue_text

@Composable
fun LoginScreen(modifier: Modifier = Modifier, navController: NavController) {
    var email by remember {
        mutableStateOf("")
    }

    var password by remember {
        mutableStateOf("")
    }
    Box(
        modifier = modifier
            .fillMaxSize()
    ) {
        Image(
            modifier = Modifier.fillMaxSize(),
            painter = painterResource(id = R.drawable.bg_screen),
            contentDescription = "Splash Screen",
            contentScale = ContentScale.FillBounds
        )
        Box (
            modifier = Modifier
                .align(Alignment.Center)
        ){
            Column(
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Image(
                    painter = painterResource(id = R.drawable.logo_app),
                    contentDescription = "Logo",
                )
                Text(
                    text = "Đăng nhập",
                    fontSize = 46.sp,
                    fontWeight = Bold,
                    color = Blue_text,
                )

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                    },
                    label = {
                        Text(text = "Email")
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
//                        focusedContainerColor = Color.White,
                        focusedIndicatorColor = Blue_text,
                        unfocusedIndicatorColor = Blue_text,
                        unfocusedContainerColor = Color.White,
                        unfocusedTextColor = Color.Black
                    )
                )

                Spacer(modifier = Modifier.height(8.dp))

                OutlinedTextField(
                    value = password,
                    onValueChange = {
                        password = it
                    },
                    label = {
                        Text(text = "Password")
                    },
                    colors = TextFieldDefaults.colors(
                        focusedTextColor = Color.Black,
//                        focusedContainerColor = Color.White,
                        focusedIndicatorColor = Blue_text,
                        unfocusedIndicatorColor = Blue_text,
                        unfocusedContainerColor = Color.White,
                        unfocusedTextColor = Color.Black
                    )
                )
                TextButton(
                    onClick = { /*TODO*/ },
                    modifier = Modifier.align(Alignment.End)

                ) {
                    Text(text = "Quên mật khẩu", fontSize = 16.sp, color = Blue_text,  )
                }


                Button(
                    onClick = { },
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                        .width(140.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Blue_text,
                        contentColor = Color.White
                    )
                ) {
                    Text(text = "Login")
                }

                TextButton(onClick = {
                    navController.navigate("register_main_screen")
                }) {
                    Text(text = "Don't have an account, Signup")
                }

            }
        }

    }
}