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
import com.example.appmuabandocu.core.navigation.model.Screen

sealed class BottomNavItem(val route: String, val icon: ImageVector, val title: String, val requiresLogin: Boolean = false) {
    object Home : BottomNavItem(Screen.Home.route, Icons.Default.Home, "Trang chủ")
    object HomeMxh : BottomNavItem(Screen.HomeMxh.route, Icons.Default.Home, "Dạo chợ")
    object Add : BottomNavItem(Screen.Category.route, Icons.Default.Add, "Đăng bán", true)
    object Favorite : BottomNavItem(Screen.Favorite.route, Icons.Default.Favorite, "Yêu thích", true)
    object Profile : BottomNavItem(Screen.Profile.route, Icons.Default.Person, "Cá nhân", true)
}

@Composable
fun BottomNavBar(navController: NavController) {
    val items = listOf(
        BottomNavItem.Home,
        BottomNavItem.HomeMxh,
        BottomNavItem.Add,
        BottomNavItem.Favorite,
        BottomNavItem.Profile,
    )

    AnimatedVisibility(
        visible = true,
        enter = slideInVertically(initialOffsetY = { it }) + fadeIn(),
        exit = slideOutVertically(targetOffsetY = { it }) + fadeOut()
    ) {
        NavigationBar(
            modifier = Modifier.animateContentSize(
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessLow
                )
            ),
            tonalElevation = 8.dp
        ) {
            val navBackStackEntry by navController.currentBackStackEntryAsState()
            val currentDestination = navBackStackEntry?.destination

            items.forEach { item ->
                val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
                val scale by animateFloatAsState(
                    targetValue = if (selected) 1.2f else 1.0f,
                    animationSpec = tween(durationMillis = 300)
                )

                NavigationBarItem(
                    icon = {
                        Box(modifier = Modifier.scale(scale)) {
                            Icon(
                                imageVector = item.icon,
                                contentDescription = item.title,
                                tint = if (selected)
                                    Color(0xFF2196F3)
                                else
                                    Color.Gray.copy(alpha = 0.7f)
                            )
                        }
                    },
                    label = {
                        Text(
                            text = item.title,
                            color = if (selected)
                                Color(0xFF2196F3)
                            else
                                Color.Gray.copy(alpha = 0.7f),
                            modifier = Modifier.animateContentSize()
                        )
                    },
                    selected = selected,
                    onClick = {
                        if (currentDestination?.route != item.route) {
                            navController.navigate(item.route) {
                                // Pop up to the start destination of the graph to
                                // avoid building up a large stack of destinations
                                popUpTo(navController.graph.findStartDestination().id) {
                                    saveState = true
                                }
                                // Avoid multiple copies of the same destination
                                launchSingleTop = true
                                // Restore state when reselecting a previously selected item
                                restoreState = true
                            }
                        }
                    }
                )
            }
        }
    }
}