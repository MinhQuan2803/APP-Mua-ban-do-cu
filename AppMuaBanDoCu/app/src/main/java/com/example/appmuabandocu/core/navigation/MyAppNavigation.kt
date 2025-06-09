//package com.example.appmuabandocu.core.navigation
//
//import ProductViewModel
//import RegisterMainScreen
//import android.app.Activity.RESULT_OK
//import android.widget.Toast
//import androidx.activity.compose.rememberLauncherForActivityResult
//import androidx.activity.result.IntentSenderRequest
//import androidx.activity.result.contract.ActivityResultContracts
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.rememberCoroutineScope
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.platform.LocalContext
//import androidx.lifecycle.compose.collectAsStateWithLifecycle
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.compose.NavHost
//import androidx.navigation.compose.composable
//import androidx.navigation.compose.rememberNavController
//import com.example.appmuabandocu.core.ui.SplashRoleScreen
//import com.example.appmuabandocu.core.ui.SplashScreen
//import com.example.appmuabandocu.feature_add_product.ui.AddProductScreen
//import com.example.appmuabandocu.feature_add_product.ui.CategoryScreen
//import com.example.appmuabandocu.feature_auth.LoginScreen
//import com.example.appmuabandocu.feature_favorite.FavoriteScreen
//import com.example.appmuabandocu.feature_home.HomeScreen
//import com.example.appmuabandocu.feature_product.ProductDetailScreen
//import com.example.appmuabandocu.feature_profile.ManageProductScreen
//import com.example.appmuabandocu.feature_profile.ProfileDetailScreen
//import com.example.appmuabandocu.viewmodel.FavoriteViewModel
//import com.google.firebase.auth.ktx.auth
//import com.google.firebase.auth.FirebaseAuth
//import kotlinx.coroutines.coroutineScope
//import kotlinx.coroutines.launch
//
//@Composable
//fun MyAppNavigation(modifier: Modifier = Modifier,googleAuthUiClient: GoogleAuthUiClient) {
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