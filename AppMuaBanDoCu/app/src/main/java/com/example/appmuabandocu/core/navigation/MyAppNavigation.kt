package com.example.appmuabandocu.core.navigation

import android.app.Activity.RESULT_OK
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.IntentSenderRequest
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContentTransitionScope
import androidx.compose.animation.EnterTransition
import androidx.compose.animation.ExitTransition
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavBackStackEntry
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.example.appmuabandocu.core.navigation.model.Screen
import com.example.appmuabandocu.core.ui.SplashRoleScreen
import com.example.appmuabandocu.core.ui.SplashScreen
import com.example.appmuabandocu.feature_add_product.ui.AddProductScreen
import com.example.appmuabandocu.feature_add_product.ui.CategoryScreen
import com.example.appmuabandocu.feature_auth.LoginScreen
import com.example.appmuabandocu.feature_auth.RegisterMainScreen
import com.example.appmuabandocu.feature_favorite.FavoriteScreen
import com.example.appmuabandocu.feature_home.HomeScreen
import com.example.appmuabandocu.feature_mxh.MxhScreen
import com.example.appmuabandocu.feature_product.ProductDetailScreen
import com.example.appmuabandocu.feature_profile.ManageProductScreen
import com.example.appmuabandocu.feature_profile.ProfileDetailScreen
import com.example.appmuabandocu.feature_profile.ProfileScreen
import com.example.appmuabandocu.viewmodel.AuthViewModel
import com.example.appmuabandocu.viewmodel.FavoriteViewModel
import com.example.appmuabandocu.viewmodel.ManageProductViewModel
import com.example.appmuabandocu.viewmodel.ProductViewModel
import com.example.appmuabandocu.viewmodel.ProfileViewModel
import com.google.firebase.auth.ktx.auth
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.coroutineScope
import kotlinx.coroutines.launch

private const val THOI_GIAN_CHUYEN_DONG = 500

private fun AnimatedContentTransitionScope<NavBackStackEntry>.taoHieuUngVao(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(THOI_GIAN_CHUYEN_DONG)
    ) + fadeIn(animationSpec = tween(THOI_GIAN_CHUYEN_DONG))
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.taoHieuUngRa(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Left,
        animationSpec = tween(THOI_GIAN_CHUYEN_DONG)
    ) + fadeOut(animationSpec = tween(THOI_GIAN_CHUYEN_DONG))
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.taoHieuUngQuayLai(): EnterTransition {
    return slideIntoContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(THOI_GIAN_CHUYEN_DONG)
    ) + fadeIn(animationSpec = tween(THOI_GIAN_CHUYEN_DONG))
}

private fun AnimatedContentTransitionScope<NavBackStackEntry>.taoHieuUngThoatQuayLai(): ExitTransition {
    return slideOutOfContainer(
        towards = AnimatedContentTransitionScope.SlideDirection.Right,
        animationSpec = tween(THOI_GIAN_CHUYEN_DONG)
    ) + fadeOut(animationSpec = tween(THOI_GIAN_CHUYEN_DONG))
}

@Composable
fun MyAppNavigation(
) {

    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val authViewModel: AuthViewModel = viewModel()
    val productViewModel: ProductViewModel = viewModel()

    val isLoggedIn by authViewModel.isLoggedIn.collectAsState()

    val context = LocalContext.current


    NavHost(navController = navController, startDestination = Screen.Splash.route) {
        composable(
            Screen.Splash.route ,
            enterTransition = { taoHieuUngVao() },
            exitTransition = { taoHieuUngRa() },
            popEnterTransition = { taoHieuUngQuayLai() },
            popExitTransition = { taoHieuUngThoatQuayLai() }
        ) {
            SplashScreen(onSplashFinished = {
                navController.navigate(Screen.SplashRole.route) {
                    popUpTo(Screen.Splash.route) { inclusive = true }
                }
            })
        }
        composable(
            Screen.SplashRole.route,
            enterTransition = { taoHieuUngVao() },
            exitTransition = { taoHieuUngRa() },
            popEnterTransition = { taoHieuUngQuayLai() },
            popExitTransition = { taoHieuUngThoatQuayLai() }
        ) {
            SplashRoleScreen(
                navController
            )
        }
        composable(
            Screen.Login.route,
            enterTransition = { taoHieuUngVao() },
            exitTransition = { taoHieuUngRa() },
            popEnterTransition = { taoHieuUngQuayLai() },
            popExitTransition = { taoHieuUngThoatQuayLai() }
        ) {
            LoginScreen(
                authViewModel,
                navController,
            )
        }
        composable(
            Screen.Register.route,
            enterTransition = { taoHieuUngVao() },
            exitTransition = { taoHieuUngRa() },
            popEnterTransition = { taoHieuUngQuayLai() },
            popExitTransition = { taoHieuUngThoatQuayLai() }
        ) {
            RegisterMainScreen(
                navController = navController,
                authViewModel = authViewModel
            )
        }

        composable(
            Screen.HomeMxh.route,
            enterTransition = { taoHieuUngVao() },
            exitTransition = { taoHieuUngRa() },
            popEnterTransition = { taoHieuUngQuayLai() },
            popExitTransition = { taoHieuUngThoatQuayLai() }
        ) {
            MxhScreen(
                navController = navController,
                productViewModel = productViewModel,
            )
        }
        composable(Screen.Home.route,
            enterTransition = { taoHieuUngVao() },
            exitTransition = { taoHieuUngRa() },
            popEnterTransition = { taoHieuUngQuayLai() },
            popExitTransition = { taoHieuUngThoatQuayLai() }
        ) {
            HomeScreen(
                navController = navController
            )
        }
        composable(Screen.AddProduct.route) {
            AddProductScreen(
                category = "",
                navController = navController
            )
        }
        composable(Screen.Category.route,
            enterTransition = { taoHieuUngVao() },
            exitTransition = { taoHieuUngRa() },
            popEnterTransition = { taoHieuUngQuayLai() },
            popExitTransition = { taoHieuUngThoatQuayLai() }
        ) {
            CategoryScreen(
                navController = navController,
                auth = auth // Pass FirebaseAuth instance
            )
        }
        composable(Screen.Favorite.route,
            enterTransition = { taoHieuUngVao() },
            exitTransition = { taoHieuUngRa() },
            popEnterTransition = { taoHieuUngQuayLai() },
            popExitTransition = { taoHieuUngThoatQuayLai() }
        ) {
            FavoriteScreen(
                navController = navController,
                viewModel = ProductViewModel(),
                favoriteViewModel = FavoriteViewModel(),
            )
        }

        composable(Screen.Profile.route,
            enterTransition = { taoHieuUngVao() },
            exitTransition = { taoHieuUngRa() },
            popEnterTransition = { taoHieuUngQuayLai() },
            popExitTransition = { taoHieuUngThoatQuayLai() }
        ) {
            ProfileScreen(
                auth = auth,
                onSignOut = {
                    authViewModel.signOut(context)
                    navController.navigate(Screen.Login.route) {
                        popUpTo(Screen.Profile.route) { inclusive = true }
                    }
                },
                navController = navController,
            )
        }
        composable(Screen.ManageProduct.route) {
            ManageProductScreen(
                navController = navController,
                viewModel = ManageProductViewModel()
            )
        }
        composable(Screen.ProfileDetail.route,
            enterTransition = { taoHieuUngVao() },
            exitTransition = { taoHieuUngRa() },
            popEnterTransition = { taoHieuUngQuayLai() },
            popExitTransition = { taoHieuUngThoatQuayLai() }
        ) {
            ProfileDetailScreen(
                navController,
                auth,
                profileViewModel = ProfileViewModel()
            )
        }

        composable(Screen.ProductDetail.route) { backStackEntry ->
            val productId = backStackEntry.arguments?.getString("productId") ?: ""
            ProductDetailScreen(navController = navController, id = productId)
        }

    }

}






















//    val navController = rememberNavController()
//    val auth = FirebaseAuth.getInstance()
//    val coroutineScope = rememberCoroutineScope()
//    val context = LocalContext.current
//    NavHost(
//        navController = navController,
//        startDestination = "splash_screen"
//    ) {
//        composable("splash_screen") { SplashScreen(modifier, navController) }
//        composable("splash_role_screen") { SplashRoleScreen(modifier, navController) }
//
//        composable("login_screen") {
//            val viewModel = viewModel<SignInViewModel>()
//            val state by viewModel.state.collectAsStateWithLifecycle()
//
//            LaunchedEffect(key1 = Unit) {
//                if(googleAuthUiClient.getSignedInUser() != null) {
//                    navController.navigate("homeNav")
//                }
//            }
//
//            val launcher = rememberLauncherForActivityResult(
//                contract = ActivityResultContracts.StartIntentSenderForResult(),
//                onResult = { result ->
//                    if (result.resultCode == RESULT_OK) {
//                        coroutineScope.launch {
//                            val signInResult = googleAuthUiClient.signInWithIntent(
//                                intent = result.data ?: return@launch
//                            )
//                            viewModel.onSignInResult(signInResult)
//                        }
//                    }
//                }
//            )
//
//            LaunchedEffect(state.isSignInSuccessful) {
//                if (state.isSignInSuccessful) {
//                    Toast.makeText(context, "Đăng nhập thành công", Toast.LENGTH_LONG).show()
//                    navController.navigate("homeNav")
//                    viewModel.resetState()
//                }
//            }
//
//            LoginScreen(
//                modifier = modifier,
//                state = state,
//                onSignInClick = {
//                    coroutineScope.launch {
//                        val signInIntentSender = googleAuthUiClient.signIn()
//                        launcher.launch(
//                            IntentSenderRequest.Builder(
//                                signInIntentSender ?: return@launch
//                            ).build()
//                        )
//                    }
//                },
//                navController
//            )
//        }
//        composable("register_main_screen") { RegisterMainScreen(modifier, navController) }
//
//        composable("home_screen") { HomeScreen(modifier, navController) }
//        composable("homeNav") { BottomNavigationBar(modifier, navController) }
//
//        composable("add_product_screen") { AddProductScreen(category = "",navController = navController) }
//
//        composable("favorite_screen") {
//            FavoriteScreen(
//                navController = navController,
//                viewModel = ProductViewModel(),
//                favoriteViewModel = FavoriteViewModel()
//            )
//        }
//        composable("category_screen") {
//            CategoryScreen(
//                auth = auth,  // Truyền FirebaseAuth
//                navController = navController
//            )
//        }
//        composable("add_product_screen/{category}") { backStackEntry ->
//            val category = backStackEntry.arguments?.getString("category") ?: ""
//            AddProductScreen(category = category,navController = navController)
//        }
//
//        composable(
//            route = "product_detail/{id}"
//        ) { backStackEntry ->
//            val id = backStackEntry.arguments?.getString("id") ?: ""
//            ProductDetailScreen(navController = navController, id = id)
//        }
//
//        composable("profile_detail") { ProfileDetailScreen(modifier, navController, auth) }
//        composable("profile_manage_screen") {
//            ManageProductScreen(viewModel = viewModel(), navController = navController)
//        }
//    }
//}