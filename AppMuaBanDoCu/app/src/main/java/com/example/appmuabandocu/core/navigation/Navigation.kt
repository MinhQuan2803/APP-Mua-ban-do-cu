package com.example.appmuabandocu.core.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.Crossfade
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
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

// Danh sách các route chính hiển thị bottom navigation
val mainTabRoutes = listOf(
    "homeNav",
    "home_mxh",
    "category_screen",
    "favorite_screen",
    "profile_screen"
)

// Danh sách các tab yêu cầu đăng nhập
val tabsRequireLogin = listOf(
    "category_screen",
    "favorite_screen",
    "profile_screen"
)

// Lưu trữ tab trước khi chuyển sang đăng nhập
var lastTab = "homeNav"

@Composable
fun Navigation(
    modifier: Modifier = Modifier,
) {
    val authViewModel = viewModel<AuthViewModel>()
    val navController = rememberNavController()
    val auth = FirebaseAuth.getInstance()
    val context = LocalContext.current

    // Kiểm tra trạng thái đăng nhập
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    // Kiểm tra xem người dùng đã đăng nhập chưa
    val isUserLoggedIn = remember(auth.currentUser) {
        auth.currentUser != null
    }

    // Kiểm tra xem route hiện tại có phải là một trong các route chính không
    val isMainTab = remember(currentRoute) {
        mainTabRoutes.any { route ->
            currentRoute == route
        }
    }

    // Sử dụng NavHost duy nhất cho tất cả màn hình
    NavHost(
        navController = navController,
        startDestination = "splash_screen"
    ) {
        // Màn hình Splash, Login, Register
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
            LoginScreen(
                modifier = modifier,
                authViewModel = authViewModel,
                navController = navController,
                // Khi quay lại từ màn hình đăng nhập, trở về màn hình Home
                onBack = {
                    navController.navigate("homeNav") {
                        popUpTo("login_screen") { inclusive = true }
                    }
                }
            )
        }

        composable("register_screen") {
            RegisterMainScreen(
                modifier,
                navController,
                addressViewModel = AddressViewModel(),
                authViewModel
            )
        }

        // Màn hình Tab chính với Bottom Navigation
        composable("homeNav") {
            MainScreenWithBottomNav(
                navController = navController,
                auth = auth,
                context = context,
                authViewModel = authViewModel,
                modifier = modifier,
                selectedTab = "homeNav",
                isUserLoggedIn = isUserLoggedIn
            )
        }

        composable("home_mxh") {
            MainScreenWithBottomNav(
                navController = navController,
                auth = auth,
                context = context,
                authViewModel = authViewModel,
                modifier = modifier,
                selectedTab = "home_mxh",
                isUserLoggedIn = isUserLoggedIn
            )
        }

        composable("favorite_screen") {
            // Kiểm tra đăng nhập trước khi truy cập tab Yêu thích
            LaunchedEffect(Unit) {
                if (!isUserLoggedIn) {
                    lastTab = "favorite_screen"
                    navController.navigate("login_screen")
                }
            }

            if (isUserLoggedIn) {
                MainScreenWithBottomNav(
                    navController = navController,
                    auth = auth,
                    context = context,
                    authViewModel = authViewModel,
                    modifier = modifier,
                    selectedTab = "favorite_screen",
                    isUserLoggedIn = isUserLoggedIn
                )
            }
        }

        composable("category_screen") {
            // Kiểm tra đăng nhập trước khi truy cập tab Đăng bán
            LaunchedEffect(Unit) {
                if (!isUserLoggedIn) {
                    lastTab = "category_screen"
                    navController.navigate("login_screen")
                }
            }

            if (isUserLoggedIn) {
                MainScreenWithBottomNav(
                    navController = navController,
                    auth = auth,
                    context = context,
                    authViewModel = authViewModel,
                    modifier = modifier,
                    selectedTab = "category_screen",
                    isUserLoggedIn = isUserLoggedIn
                )
            }
        }

        composable("profile_screen") {
            // Kiểm tra đăng nhập trước khi truy cập tab Cá nhân
            LaunchedEffect(Unit) {
                if (!isUserLoggedIn) {
                    lastTab = "profile_screen"
                    navController.navigate("login_screen")
                }
            }

            if (isUserLoggedIn) {
                MainScreenWithBottomNav(
                    navController = navController,
                    auth = auth,
                    context = context,
                    authViewModel = authViewModel,
                    modifier = modifier,
                    selectedTab = "profile_screen",
                    isUserLoggedIn = isUserLoggedIn
                )
            }
        }

        // Các màn hình chi tiết
        composable("add_product_screen/{category}") { backStackEntry ->
            val category = backStackEntry.arguments?.getString("category") ?: ""
            AddProductScreen(category = category, navController = navController)
        }

        composable("profile_detail") {
            ProfileDetailScreen(
                modifier,
                navController,
                auth,
                profileViewModel = ProfileViewModel()
            )
        }

        composable(
            route = "product_detail/{id}"
        ) { backStackEntry ->
            val id = backStackEntry.arguments?.getString("id") ?: ""
            ProductDetailScreen(navController = navController, id = id)
        }

        composable("manage_product") {
            ManageProductScreen(
                navController = navController,
                viewModel = ManageProductViewModel()
            )
        }
    }
}

@Composable
fun MainScreenWithBottomNav(
    navController: NavController,
    auth: FirebaseAuth,
    context: android.content.Context,
    authViewModel: AuthViewModel,
    modifier: Modifier,
    selectedTab: String,
    isUserLoggedIn: Boolean
) {
    // Tạo các view model ở mức cao nhất để chỉ khởi tạo một lần
    val productViewModel = remember { ProductViewModel() }
    val searchViewModel = remember { SearchProductViewModel() }
    val favoriteViewModel = remember { FavoriteViewModel() }

    // Sử dụng BottomNavigation để hiển thị thanh điều hướng
    BottomNavigation(
        navController = navController,
        isUserLoggedIn = isUserLoggedIn,
        onRequireLogin = { tab ->
            lastTab = tab
            navController.navigate("login_screen")
        }
    ) {
        // Sử dụng NavHostContainer để giữ trạng thái của tất cả các tab
        NavHostContainer(
            selectedTab = selectedTab,
            navController = navController,
            auth = auth,
            context = context,
            authViewModel = authViewModel,
            modifier = modifier,
            productViewModel = productViewModel,
            searchViewModel = searchViewModel,
            favoriteViewModel = favoriteViewModel
        )
    }
}

