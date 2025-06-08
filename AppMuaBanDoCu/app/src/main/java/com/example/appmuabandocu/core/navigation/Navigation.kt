package com.example.appmuabandocu.core.navigation

import RegisterMainScreen
import android.content.ClipData
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmuabandocu.core.ui.SplashScreen
import com.example.appmuabandocu.feature_add_product.ui.AddProductScreen
import com.example.appmuabandocu.feature_add_product.ui.CategoryScreen
import com.example.appmuabandocu.feature_auth.AuthViewModel
import com.example.appmuabandocu.feature_auth.ui.LoginScreen
import com.example.appmuabandocu.feature_favorite.ui.FavoriteScreen
import com.example.appmuabandocu.feature_home.ui.HomeScreen
import com.example.appmuabandocu.viewmodel.FavoriteViewModel
import com.example.appmuabandocu.viewmodel.ProductViewModel
import com.example.appmuabandocu.viewmodel.SearchProductViewModel
import com.google.firebase.auth.FirebaseAuth

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
) {
    val authViewModel = viewModel<AuthViewModel>()
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()

    NavHost(
        navController = navController,
        startDestination = "splash_screen"
    ) {
        composable("splash_screen") {
            SplashScreen(modifier, navController)
        }
        composable("login_screen") {
            LoginScreen(modifier, authViewModel, navController)
        }
        composable("register_screen") {
            RegisterMainScreen(modifier, navController, authViewModel)
        }
        composable("homeNav") {
            BottomNavigation(navController = navController) {
                HomeScreen(
                    modifier = modifier,
                    navController = navController,
                    productViewModel = ProductViewModel(),
                    searchViewModel = SearchProductViewModel()
                )
            }
        }
        composable("add_product_screen") { AddProductScreen(category = "",navController = navController) }

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

    }

}

@Composable
fun TestScreen(
    modifier: Modifier = Modifier,
    navController: androidx.navigation.NavController,
    authViewModel: AuthViewModel
) {
    val context = LocalContext.current
    // Placeholder content for the home screen
    Text(
        text = "Welcome to the Home Screen!",
        modifier = modifier.padding(16.dp)
    )
    Button(
        onClick = { authViewModel.signOut(context)
            navController.navigate("login_screen") },
        modifier = Modifier.padding(16.dp)
    ) {

    }
}