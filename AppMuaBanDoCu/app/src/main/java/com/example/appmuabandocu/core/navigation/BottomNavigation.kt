package com.example.appmuabandocu.core.navigation

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateContentSize
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.animation.slideInVertically
import androidx.compose.animation.slideOutVertically
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.calculateEndPadding
import androidx.compose.foundation.layout.calculateStartPadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.Home
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.DisposableEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalLayoutDirection
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.spring
import androidx.compose.ui.draw.scale

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String, val requiresLogin: Boolean = false) {
    object Home : BottomNavItem("homeNav", Icons.Default.Home, "Trang chủ")
    object HomeMxh : BottomNavItem("home_mxh", Icons.Default.Home, "Dạo chợ")
    object Add : BottomNavItem("category_screen", Icons.Default.Add, "Đăng bán", true)
    object Favorite : BottomNavItem("favorite_screen", Icons.Default.Favorite, "Yêu thích", true)
    object Profile : BottomNavItem("profile_screen", Icons.Default.Person, "Cá nhân", true)
}

// Routes that should hide the bottom nav
val routesWithoutBottomNav = listOf(
    "login_screen",
    "register_screen",
    "product_detail"
)

@Composable
fun BottomNavigation(
    navController: NavController,
    isUserLoggedIn: Boolean = false,
    onRequireLogin: (String) -> Unit = {},
    content: @Composable () -> Unit
) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.HomeMxh,
        BottomNavItem.Add,
        BottomNavItem.Favorite,
        BottomNavItem.Profile
    )

    // Track current route to determine if bottom nav should be visible
    val navBackStackEntry by navController.currentBackStackEntryAsState()
    val currentRoute = navBackStackEntry?.destination?.route ?: ""

    // Check if the current route should show bottom navigation
    val shouldShowBottomNav = remember(currentRoute) {
        !routesWithoutBottomNav.any { route ->
            currentRoute.contains(route)
        }
    }

    var bottomNavVisible by rememberSaveable { mutableStateOf(shouldShowBottomNav) }

    DisposableEffect(shouldShowBottomNav) {
        bottomNavVisible = shouldShowBottomNav
        onDispose { }
    }

    val layoutDirection = LocalLayoutDirection.current

    // Sử dụng animateContentSize để có animation mượt hơn khi kích thước thay đổi
    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = shouldShowBottomNav,
                enter = fadeIn(animationSpec = tween(300)) + slideInVertically(
                    animationSpec = tween(300),
                    initialOffsetY = { it }
                ),
                exit = fadeOut(animationSpec = tween(300)) + slideOutVertically(
                    animationSpec = tween(300),
                    targetOffsetY = { it }
                )
            ) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    val currentDestination = navBackStackEntry?.destination

                    items.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

                        NavigationBarItem(
                            icon = {
                                // Thêm animation cho icon
                                val scale by animateFloatAsState(
                                    if (selected) 1.2f else 1f,
                                    animationSpec = spring(
                                        dampingRatio = Spring.DampingRatioMediumBouncy,
                                        stiffness = Spring.StiffnessMedium
                                    )
                                )
                                Icon(
                                    item.icon,
                                    contentDescription = item.label,
                                    modifier = Modifier.scale(scale)
                                )
                            },
                            label = { Text(item.label) },
                            selected = selected,
                            onClick = {
                                if (item.requiresLogin && !isUserLoggedIn) {
                                    // Nếu tab yêu cầu đăng nhập và người dùng chưa đăng nhập
                                    // thì chuyển đến màn hình đăng nhập
                                    onRequireLogin(item.route)
                                } else if (currentDestination?.route != item.route) {
                                    // Chuyển đến tab được chọn
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                } else {
                                    // Xử lý khi click vào tab hiện tại
                                    // Có thể gửi event để scroll lên đầu trang
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        // Sử dụng animateContentSize để animation mượt hơn khi padding thay đổi
        Box(
            modifier = Modifier
                .padding(
                    bottom = if (shouldShowBottomNav) innerPadding.calculateBottomPadding() else 0.dp,
                    top = innerPadding.calculateTopPadding(),
                    start = innerPadding.calculateStartPadding(layoutDirection),
                    end = innerPadding.calculateEndPadding(layoutDirection)
                )
                .animateContentSize(
                    animationSpec = spring(
                        dampingRatio = Spring.DampingRatioLowBouncy,
                        stiffness = Spring.StiffnessMedium
                    )
                )
        ) {
            content()
        }
    }
}