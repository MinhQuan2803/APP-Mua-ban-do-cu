package com.example.appmuabandocu.core.navigation.model

import androidx.compose.ui.graphics.vector.ImageVector
import okhttp3.Route

data class ItemsNav(
    val route: Route,
    val label: String,
    val icon: ImageVector
)