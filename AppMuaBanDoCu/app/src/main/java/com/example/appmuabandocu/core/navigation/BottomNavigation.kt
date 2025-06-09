package com.example.appmuabandocu.core.navigation

import androidx.compose.animation.AnimatedVisibility
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

sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
object Home : BottomNavItem("homeNav", Icons.Default.Home, "Trang chủ")
object HomeMxh : BottomNavItem("home_mxh", Icons.Default.Home, "Dạo chợ")
object Add : BottomNavItem("category_screen", Icons.Default.Add, "Đăng bán")
object Favorite : BottomNavItem("favorite_screen", Icons.Default.Favorite, "Yêu thích")
object Profile : BottomNavItem("profile_screen", Icons.Default.Person, "Cá nhân")
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

    Scaffold(
        bottomBar = {
            AnimatedVisibility(
                visible = bottomNavVisible,
                enter = fadeIn() + slideInVertically { it },
                exit = fadeOut() + slideOutVertically { it }
            ) {
                NavigationBar(
                    containerColor = Color.White,
                    tonalElevation = 8.dp
                ) {
                    val currentDestination = navBackStackEntry?.destination

                    items.forEach { item ->
                        val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true

                        NavigationBarItem(
                            icon = { Icon(item.icon, contentDescription = item.label) },
                            label = { Text(item.label) },
                            selected = selected,
                            onClick = {
                                if (currentDestination?.route != item.route) {
                                    navController.navigate(item.route) {
                                        popUpTo(navController.graph.findStartDestination().id) {
                                            saveState = true
                                        }
                                        launchSingleTop = true
                                        restoreState = true
                                    }
                                }
                            }
                        )
                    }
                }
            }
        }
    ) { innerPadding ->
        Box(modifier = Modifier.padding(
            bottom = if (bottomNavVisible) innerPadding.calculateBottomPadding() else 0.dp,
            top = innerPadding.calculateTopPadding(),
            start = innerPadding.calculateStartPadding(layoutDirection),
            end = innerPadding.calculateEndPadding(layoutDirection)
        )) {
            content()
        }
    }
}