package com.example.appmuabandocu.core.navigation

import androidx.compose.animation.Crossfade
import androidx.compose.animation.core.tween
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
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
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow

// Tạo một StateFlow để theo dõi trạng thái đăng nhập
private val _authStateFlow = MutableStateFlow(FirebaseAuth.getInstance().currentUser != null)
val authStateFlow: StateFlow<Boolean> = _authStateFlow.asStateFlow()

// Hàm cập nhật trạng thái đăng nhập
fun updateAuthState() {
    _authStateFlow.value = FirebaseAuth.getInstance().currentUser != null
}

// T����o đối tượng AuthStateManager để quản lý trạng th��i đăng nhập
object AuthStateManager {
    init {
        FirebaseAuth.getInstance().addAuthStateListener { firebaseAuth ->
            updateAuthState()
        }
    }
}


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

    // Kiểm tra trạng thái đăng nhập từ StateFlow
    val isUserLoggedIn by authStateFlow.collectAsState()

    // Cập nhật trạng thái đăng nhập khi component được t���o
    LaunchedEffect(Unit) {
        updateAuthState()
    }

    // Kiểm tra trạng thái đăng nhập khi component được recompose
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    // S�� dụng NavHost duy nhất cho tất cả màn hình
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
            // Kiểm tra đăng nhập trước khi truy cập tab Yêu thích và thêm delay nhỏ
            LaunchedEffect(isUserLoggedIn) {
                // Thêm một delay nhỏ để đảm bảo trạng thái đăng nhập đã được cập nhật
                delay(100)
                if (!isUserLoggedIn) {
                    lastTab = "favorite_screen"
                    navController.navigate("login_screen")
                }
            }

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

        composable("category_screen") {
            // Kiểm tra đăng nhập trước khi truy c���p tab Đăng bán và thêm delay nhỏ
            LaunchedEffect(isUserLoggedIn) {
                // Thêm một delay nhỏ để đảm bảo trạng thái đăng nhập đã được cập nhật
                delay(100)
                if (!isUserLoggedIn) {
                    lastTab = "category_screen"
                    navController.navigate("login_screen")
                }
            }

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

        composable("profile_screen") {
            // Ki��m tra đăng nhập trước khi truy cập tab Cá nhân và thêm delay nhỏ
            LaunchedEffect(isUserLoggedIn) {
                // Thêm một delay nhỏ để đảm bảo trạng thái đăng nhập đã được cập nhật
                delay(100)
                if (!isUserLoggedIn) {
                    lastTab = "profile_screen"
                    navController.navigate("login_screen")
                }
            }

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

@Composable
fun NavHostContainer(
    selectedTab: String,
    navController: NavController,
    auth: FirebaseAuth,
    context: android.content.Context,
    authViewModel: AuthViewModel,
    modifier: Modifier,
    productViewModel: ProductViewModel,
    searchViewModel: SearchProductViewModel,
    favoriteViewModel: FavoriteViewModel
) {
    // Sử dụng Crossfade để tạo hiệu ứng chuyển đổi mượt mà
    Crossfade(
        targetState = selectedTab,
        animationSpec = tween(150)
    ) { currentTab ->
        when (currentTab) {
            "homeNav" -> {
                HomeScreen(
                    modifier = modifier,
                    navController = navController,
                    productViewModel = productViewModel,
                    searchViewModel = searchViewModel
                )
            }
            "home_mxh" -> {
                MxhScreen(
                    modifier = modifier,
                    navController = navController,
                    productViewModel = productViewModel,
                    searchViewModel = searchViewModel
                )
            }
            "favorite_screen" -> {
                FavoriteScreen(
                    navController = navController,
                    viewModel = productViewModel,
                    favoriteViewModel = favoriteViewModel
                )
            }
            "category_screen" -> {
                CategoryScreen(
                    auth = auth,
                    navController = navController
                )
            }
            "profile_screen" -> {
                ProfileScreen(
                    auth = auth,
                    onSignOut = {
                        authViewModel.signOut(context)
                        navController.navigate("login_screen"){
                            popUpTo("homeNav") { inclusive = true }
                        }
                    },
                    navController = navController,
                )
            }
        }
    }
}
