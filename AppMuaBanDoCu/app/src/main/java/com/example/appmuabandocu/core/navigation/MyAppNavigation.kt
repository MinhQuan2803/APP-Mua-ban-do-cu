package com.example.appmuabandocu.core.navigation

import ProductViewModel
import RegisterMainScreen
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
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
import com.example.appmuabandocu.feature_product.ui.ProductDetailScreen
import com.example.appmuabandocu.feature_profile.ui.ManageProductScreen
import com.example.appmuabandocu.feature_profile.ui.ProfileDetailScreen
import com.example.appmuabandocu.viewmodel.FavoriteViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.FirebaseAuth
@Composable
fun MyAppNavigation(modifier: Modifier = Modifier) {
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    NavHost(
        navController = navController,
        startDestination = "splash_screen"
    ) {
        composable("splash_screen") { SplashScreen(modifier, navController) }
        composable("splash_role_screen") { SplashRoleScreen(modifier, navController) }

        composable("login_screen") { LoginScreen(modifier, navController) }
        composable("register_main_screen") { RegisterMainScreen(modifier, navController) }

        composable("home_screen") { HomeScreen(modifier, navController) }
        composable("homeNav") { BottomNavigationBar(modifier, navController) }

        composable("add_product_screen") { AddProductScreen(modifier, "Đồ bán") }

        composable("favorite_screen") {
            FavoriteScreen(
                navController = navController,
                viewModel = ProductViewModel(),
                favoriteViewModel = FavoriteViewModel()
            )
        }
        composable("category_screen") {
            CategoryScreen(
                auth = auth,  // Truyền FirebaseAuth
                navController = navController
            )
        }
        composable("add_product_screen/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            AddProductScreen(category = category)
        }

        composable(
            route = "product_detail/{id}"
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            ProductDetailScreen(navController = navController, id = id)
        }

        composable("profile_detail") { ProfileDetailScreen(modifier, navController, auth) }
        composable("profile_manage_screen") {
            ManageProductScreen(viewModel = viewModel(), navController = navController)
        }
    }
}