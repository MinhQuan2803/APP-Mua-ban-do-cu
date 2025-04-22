package com.example.appmuabandocu.core.navigation

import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.navigationBarsPadding
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.sp
import androidx.navigation.NavHostController
import com.example.appmuabandocu.R
import com.example.appmuabandocu.feature_add_product.ui.CategoryScreen
import com.example.appmuabandocu.feature_favorite.ui.TincuabanScreen
import com.example.appmuabandocu.feature_home.ui.HomeScreen
import com.example.appmuabandocu.feature_mxh.ui.MxhScreen
import com.example.appmuabandocu.feature_profile.ui.ProfileScreen
import com.example.appmuabandocu.ui.theme.Blue_text
import com.google.firebase.auth.FirebaseAuth


data class NavItem(val route: String, val icon: Int, val label: String)

@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier, navController: NavHostController) {
    val navItemList = listOf(
        NavItem("home", R.drawable.ic_home, "Home"),
        NavItem("store", R.drawable.ic_cart, "Chợ"),
        NavItem("add", R.drawable.ic_camera, "Đăng"),
        NavItem("manage", R.drawable.ic_manage, "Bài viết"),
        NavItem("profile", R.drawable.ic_person, "Tui")
    )
    var selectedIndex by remember { mutableStateOf(0) }
    Scaffold(
        Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .navigationBarsPadding() // <- Đặt cái này TRƯỚC .height()
                    .height(65.dp)// 56dp là chiều cao mặc định, 80dp hơi to nên dễ bị đè
                .padding(bottom = 4.dp),
                tonalElevation = 12.dp,
                containerColor = Color(0xFFFFFFFF),
            ){

                navItemList.forEachIndexed { index, item ->
                    NavigationBarItem(
                        colors = NavigationBarItemDefaults.colors(
                            selectedIconColor = Blue_text,
                            selectedTextColor = Blue_text,
                            indicatorColor = Color.White,
                            unselectedIconColor = Color.Gray,
                            unselectedTextColor = Color.Gray
                        ),

                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index

                        },
                        icon = { Icon(painterResource(id = item.icon),
                            modifier = Modifier.size(30.dp),
                            contentDescription = item.label) },
                        label = {
                            Text(
                                text = item.label,
                                fontSize = 12.sp, // Kích thước chữ đồng nhất
                                fontWeight = FontWeight.Normal // Đồng nhất font-weight
                            )
                        }, alwaysShowLabel = true
                    )
                }
            }
        },
    ) { innerPadding ->
        ContentScreen(modifier = Modifier.padding(innerPadding),navController = navController, selectedIndex, auth = FirebaseAuth.getInstance())


    }
}

@Composable
fun ContentScreen(
    modifier: Modifier = Modifier,
    navController: NavHostController,
    selectedIndex: Int,
    auth: FirebaseAuth // Thêm auth vào tham số
) {
    when (selectedIndex) {
        0 -> HomeScreen(modifier = modifier, navController = navController)
        1 -> MxhScreen(
            modifier = modifier,
            navController = navController
        )
        2 -> CategoryScreen(
            auth = auth,  // Truyền FirebaseAuth
            navController = navController
        )
        3 -> TincuabanScreen(
            auth = auth,  // Truyền FirebaseAuth
            onSignIn = { navController.navigate("login_screen") }, // Điều hướng đến login khi đăng nhập
            onSignOut = {
                auth.signOut()  // Đăng xuất khỏi Firebase
                navController.navigate("login_screen")  // Điều hướng về login
            },
            navController = navController
        )
        4 -> ProfileScreen(
            auth = auth,  // Truyền FirebaseAuth
            onSignIn = { navController.navigate("login_screen") }, // Điều hướng đến login khi đăng nhập
            onSignOut = {
                auth.signOut()  // Đăng xuất khỏi Firebase
                navController.navigate("login_screen")  // Điều hướng về login
            },
            navController = navController
        )
    }
}


