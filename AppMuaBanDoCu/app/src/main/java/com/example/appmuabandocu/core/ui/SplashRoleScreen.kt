package com.example.appmuabandocu.core.ui

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.example.appmuabandocu.R
import com.example.appmuabandocu.ui.theme.Blue_text
import androidx.compose.ui.res.colorResource
import com.example.appmuabandocu.core.navigation.model.Screen
import com.example.appmuabandocu.ui.theme.Black

@Composable
fun SplashRoleScreen(
    navController: NavController
) {
    Box(
        modifier = Modifier
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
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Image(
                        painter = painterResource(id = R.drawable.logo_app),
                        contentDescription = "Logo",
                    )
                    Text(
                        text = "Bạn muốn ?",
                        fontSize = 45.sp,
                        fontWeight = Bold,
                        color = Blue_text,
                    )
                }
                Spacer(modifier = Modifier.height(20.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Blue_text
                    ),
                    border = BorderStroke(1.dp, Blue_text),

                    shape = ButtonDefaults.outlinedShape,
                    onClick = { }

                ){
                    Text(text = "Mua đồ cũ", fontSize = 24.sp,
                        fontWeight = Bold,
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = Color.White,
                        contentColor = Blue_text
                    ),
                    border = BorderStroke(1.dp, Blue_text),

                    shape = ButtonDefaults.outlinedShape,
                    onClick = { }

                ){
                    Text(text = "Bán đồ cũ", fontSize = 24.sp,
                        fontWeight = Bold,
                    )
                }
                Spacer(modifier = Modifier.height(10.dp))
                Button(
                    colors = ButtonDefaults.buttonColors(
                        containerColor = colorResource(id = R.color.mauchinh),
                        contentColor = Black
                    ),
                    border = BorderStroke(1.dp, Blue_text),
                    shape = ButtonDefaults.outlinedShape,
                    onClick = { navController.navigate(Screen.Home.route) }
                ){
                    Text(text = "Bắt đầu ngay", fontSize = 24.sp,
                        fontWeight = Bold,
                    )
                }
            }

        }

    }

}