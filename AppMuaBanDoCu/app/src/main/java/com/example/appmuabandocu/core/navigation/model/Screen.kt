package com.example.appmuabandocu.core.navigation.model

sealed class Screen(val route: String) {
    object Splash : Screen("splash")
    object SplashRole : Screen("splashRole")
    object Login : Screen("login")
    object Register : Screen("register")
    object Home : Screen("home")
    object Profile : Screen("profile")
    object AddProduct : Screen("add_product")
    object ProductDetail : Screen("product_detail/{productId}") {
        fun createRoute(productId: String) = "product_detail/$productId"
    }
    object Category : Screen("category_screen")
    object Favorite : Screen("favorite_screen")
    object ManageProduct : Screen("manage_product")
    object ProfileDetail : Screen("profile_detail/{userId}") {
        fun createRoute(userId: String) = "profile_detail/$userId"
    }
    object HomeMxh : Screen("home_mxh")
}