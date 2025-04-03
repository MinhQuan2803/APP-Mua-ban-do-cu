package com.example.appmuabandocu.core.navigation

import RegisterMainScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmuabandocu.core.ui.SplashRoleScreen
import com.example.appmuabandocu.core.ui.SplashScreen
import com.example.appmuabandocu.feature_add_product.ui.AddProductScreen
import com.example.appmuabandocu.feature_add_product.ui.CategoryScreen
import com.example.appmuabandocu.feature_auth.ui.LoginScreen
import com.example.appmuabandocu.feature_favorite.ui.FavoriteScreen
import com.example.appmuabandocu.feature_home.ui.HomeScreen
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.FirebaseAuth
@Composable
fun MyAppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
 val auth = FirebaseAuth.getInstance()
    NavHost(
        navController = navController,
        startDestination = "splash_screen"
    ){
        composable("splash_screen"){ SplashScreen(modifier, navController) }
        composable("splash_role_screen"){ SplashRoleScreen(modifier, navController) }
        composable("login_screen"){ LoginScreen(modifier, navController) }
        composable("register_main_screen"){ RegisterMainScreen(modifier, navController) }
        composable("home_screen"){ HomeScreen(modifier, navController) }
        composable("homeNav"){ BottomNavigationBar(modifier, navController) }
        composable("add_product_screen"){ AddProductScreen(modifier, "Đồ bán") }
        composable("favorite_screen"){ FavoriteScreen(modifier) }
        composable("category_screen"){ CategoryScreen(auth = auth,  // Truyền FirebaseAuth
            onSignIn = { navController.navigate("login_screen") }, // Điều hướng đến login khi đăng nhập
            onSignOut = {
                auth.signOut()  // Đăng xuất khỏi Firebase
                navController.navigate("login_screen")  // Điều hướng về login
            },
            navController = navController) }
        composable("add_product_screen/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            AddProductScreen(category = category)
        }

    }
}