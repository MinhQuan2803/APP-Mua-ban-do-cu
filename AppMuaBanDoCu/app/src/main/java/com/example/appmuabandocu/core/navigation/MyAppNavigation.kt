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
import com.example.appmuabandocu.feature_auth.ui.LoginScreen
import com.example.appmuabandocu.feature_favorite.ui.FavoriteScreen
import com.example.appmuabandocu.feature_home.ui.HomeScreen

@Composable
fun MyAppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()

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
        composable("add_product_screen"){ AddProductScreen(modifier) }
        composable("favorite_screen"){ FavoriteScreen(modifier) }

    }
}