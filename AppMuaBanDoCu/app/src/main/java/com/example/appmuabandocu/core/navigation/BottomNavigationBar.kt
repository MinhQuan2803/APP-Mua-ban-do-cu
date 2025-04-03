package com.example.appmuabandocu.core.navigation

import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.navigation.NavController
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.draw.clip
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavHostController
import com.example.appmuabandocu.R
import com.example.appmuabandocu.feature_add_product.ui.AddProductScreen
import com.example.appmuabandocu.feature_favorite.ui.FavoriteScreen
import com.example.appmuabandocu.feature_home.ui.HomeScreen
import com.example.appmuabandocu.feature_productlist.ui.ProductListScreen
import com.example.appmuabandocu.feature_profile.ui.ProfileScreen
import com.example.appmuabandocu.ui.theme.Blue_text
import com.google.firebase.auth.FirebaseAuth


data class NavItem(val route: String, val icon: Int, val label: String)

@Composable
fun BottomNavigationBar(modifier: Modifier = Modifier, navController: NavHostController) {
    val navItemList = listOf(
        NavItem("home", R.drawable.ic_home, "home"),
        NavItem("store", R.drawable.ic_cart, "store"),
        NavItem("add", R.drawable.ic_add, "add"),
        NavItem("profile", R.drawable.ic_person, "profile")
    )
    var selectedIndex by remember { mutableStateOf(0) }
    Scaffold(
        Modifier.fillMaxSize(),
        bottomBar = {
            NavigationBar(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(100.dp)
                    .clip(RoundedCornerShape(50.dp, 50.dp, 50.dp, 50.dp)),

                tonalElevation = 20.dp,
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
                            modifier = Modifier.size(35.dp),
                            contentDescription = item.label) },
//                        label = {
//                            Text(text = item.label)
//
//                        }
                    )
                }
            }
        },



        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate("category_screen") },
                containerColor = Color(0xFF2196F3),
                shape = CircleShape,
                modifier = Modifier
                    .offset(y = 45.dp)
                    .size(65.dp)
            ) {
                Icon(Icons.Default.Add, contentDescription = "Add", tint = Color.White)
            }
        },
        floatingActionButtonPosition = FabPosition.Center // FAB nằm giữa
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
        1 -> ProductListScreen()
        2 -> FavoriteScreen()
        3 -> ProfileScreen(
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


