package com.example.appmuabandocu.core.navigation

import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmuabandocu.core.ui.SplashRoleScreen
import com.example.appmuabandocu.core.ui.SplashScreen
import com.example.appmuabandocu.feature_add_product.ui.AddProductScreen
import com.example.appmuabandocu.feature_add_product.ui.CategoryScreen
import com.example.appmuabandocu.viewmodel.AuthViewModel
import com.example.appmuabandocu.feature_auth.LoginScreen
import com.example.appmuabandocu.feature_auth.RegisterMainScreen
import com.example.appmuabandocu.feature_favorite.FavoriteScreen
import com.example.appmuabandocu.feature_home.HomeScreen
import com.example.appmuabandocu.feature_mxh.MxhScreen
import com.example.appmuabandocu.feature_product.ProductDetailScreen
import com.example.appmuabandocu.feature_profile.ManageProductScreen
import com.example.appmuabandocu.viewmodel.ProfileViewModel
import com.example.appmuabandocu.feature_profile.ProfileDetailScreen
import com.example.appmuabandocu.feature_profile.ProfileScreen
import com.example.appmuabandocu.viewmodel.AddressViewModel
import com.example.appmuabandocu.viewmodel.FavoriteViewModel
import com.example.appmuabandocu.viewmodel.ManageProductViewModel
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
    val context = LocalContext.current

    NavHost(
        navController = navController,
        startDestination = "splash_screen"
    ) {
        composable("splash_screen") {
            SplashScreen(modifier, navController)
        }

        composable("splash_role_screen") {
            SplashRoleScreen(
                modifier = modifier,
                navController = navController
            )
        }
        composable("login_screen") {
            LoginScreen(modifier, authViewModel, navController)
        }
        composable("register_screen") {
            RegisterMainScreen(
                modifier,
                navController,
                addressViewModel = AddressViewModel(),
                authViewModel
            )
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
        composable("home_mxh") {
            BottomNavigation(navController = navController) {
                MxhScreen(
                    modifier = modifier,
                    navController = navController,
                    productViewModel = ProductViewModel(),
                    searchViewModel = SearchProductViewModel()
                )
            }
        }

        composable("favorite_screen") {
            BottomNavigation(navController = navController) {
                FavoriteScreen(
                    navController = navController,
                    viewModel = ProductViewModel(),
                    favoriteViewModel = FavoriteViewModel()
                )
            }
        }
        composable("category_screen") {
            BottomNavigation(navController = navController) {
                CategoryScreen(
                    auth = auth,
                    navController = navController
                )
            }
        }

        composable("add_product_screen/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            AddProductScreen(category = category,navController = navController)
        }

        composable("profile_screen") {
            BottomNavigation(navController = navController) {
                ProfileScreen(
                    auth = auth,
                    onSignOut = {
                        authViewModel.signOut(context)
                        navController.navigate("login_screen")
                    },
                    navController = navController,
                )
            }
        }

        composable("profile_detail") {
            ProfileDetailScreen(
                modifier,
                navController,
                auth,
                profileViewModel = ProfileViewModel()
            )
        }

        composable("manage_product_screen") {
            ManageProductScreen(
                viewModel = ManageProductViewModel(),
                navController = navController
            )
        }


        composable(
            route = "product_detail/{id}"
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            ProductDetailScreen(navController = navController, id = id)
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