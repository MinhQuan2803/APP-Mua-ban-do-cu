//package com.example.appmuabandocu.core.navigation
//
//import ProductViewModel
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.navigationBarsPadding
//import androidx.compose.foundation.layout.offset
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.*
//import androidx.compose.material3.FabPosition
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.Icon
//import androidx.compose.material3.Text
//import androidx.compose.runtime.Composable
//import androidx.compose.ui.graphics.Color
//import androidx.compose.material3.NavigationBar
//import androidx.compose.material3.NavigationBarItem
//import androidx.compose.material3.NavigationBarItemDefaults
//import androidx.compose.material3.Scaffold
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.unit.dp
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.graphics.vector.ImageVector
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.unit.sp
//import androidx.navigation.NavController
//import androidx.navigation.NavDestination.Companion.hierarchy
//import androidx.navigation.NavGraph.Companion.findStartDestination
//import androidx.navigation.NavHostController
//import androidx.navigation.compose.currentBackStackEntryAsState
//import com.example.appmuabandocu.R
//import com.example.appmuabandocu.feature_add_product.ui.CategoryScreen
//import com.example.appmuabandocu.feature_favorite.ui.FavoriteScreen
//import com.example.appmuabandocu.feature_home.ui.HomeScreen
//import com.example.appmuabandocu.feature_mxh.ui.MxhScreen
//import com.example.appmuabandocu.feature_profile.ui.ProfileScreen
//import com.example.appmuabandocu.ui.theme.Blue_text
//import com.google.firebase.auth.FirebaseAuth
//import com.example.appmuabandocu.viewmodel.FavoriteViewModel
//
////sealed class BottomNavItem(val route: String, val icon: ImageVector, val label: String) {
////    object Home : BottomNavItem("homeNav", Icons.Default.Home, "Trang chủ")
////    object Add : BottomNavItem("category_screen", Icons.Default.Add, "Đăng bán")
////    object Favorite : BottomNavItem("favorite_screen", Icons.Default.Favorite, "Yêu thích")
////    object Profile : BottomNavItem("profile_screen", Icons.Default.Person, "Cá nhân")
////}
//
//@Composable
//fun BottomNavigationBar(
//    navController: NavController,
//    content: @Composable () -> Unit
//) {
//
//    val items = listOf(
//        BottomNavItem.Home,
//        BottomNavItem.Add,
//        BottomNavItem.Favorite,
//        BottomNavItem.Profile
//    )
//    var selectedIndex by remember { mutableStateOf(0) }
//    Scaffold(
//        Modifier.fillMaxSize(),
//        bottomBar = {
//            NavigationBar(
//                containerColor = Color.White,
//                tonalElevation = 8.dp
//            ) {
//                val navBackStackEntry by navController.currentBackStackEntryAsState()
//                val currentDestination = navBackStackEntry?.destination
//
//                items.forEach { item ->
//                    val selected = currentDestination?.hierarchy?.any { it.route == item.route } == true
//
//                    NavigationBarItem(
//                        icon = { Icon(item.icon, contentDescription = item.label) },
//                        label = { Text(item.label) },
//                        selected = selected,
//                        onClick = {
//                            navController.navigate(item.route) {
//                                // Pop up to the start destination of the graph to
//                                // avoid building up a large stack of destinations
//                                popUpTo(navController.graph.findStartDestination().id) {
//                                    saveState = true
//                                }
//                                // Avoid multiple copies of the same destination when
//                                // reselecting the same item
//                                launchSingleTop = true
//                                // Restore state when reselecting a previously selected item
//                                restoreState = true
//                            }
//                        }
//                    )
//                }
//            }
//        }
//
//
////        bottomBar = {
////            NavigationBar(
////                modifier = Modifier
////                    .fillMaxWidth()
////                    .navigationBarsPadding() // <- Đặt cái này TRƯỚC .height()
////                    .height(65.dp)// 56dp là chiều cao mặc định, 80dp hơi to nên dễ bị đè
////                .padding(bottom = 4.dp),
////                tonalElevation = 12.dp,
////                containerColor = Color(0xFFFFFFFF),
////            ){
////
////                navItemList.forEachIndexed { index, item ->
////                    NavigationBarItem(
////                        colors = NavigationBarItemDefaults.colors(
////                            selectedIconColor = Blue_text,
////                            selectedTextColor = Blue_text,
////                            indicatorColor = Color.White,
////                            unselectedIconColor = Color.Gray,
////                            unselectedTextColor = Color.Gray
////                        ),
////
////                        selected = selectedIndex == index,
////                        onClick = {
////                            selectedIndex = index
////
////                        },
////                        icon = { Icon(painterResource(id = item.icon),
////                            modifier = Modifier.size(30.dp),
////                            contentDescription = item.label) },
////                        label = {
////                            Text(
////                                text = item.label,
////                                fontSize = 12.sp, // Kích thước chữ đồng nhất
////                                fontWeight = FontWeight.Normal // Đồng nhất font-weight
////                            )
////                        }, alwaysShowLabel = true
////                    )
////                }
////            }
////        },
//    ) { innerPadding ->
//        ContentScreen(modifier = Modifier.padding(innerPadding),navController = navController, selectedIndex, auth = FirebaseAuth.getInstance())
//
//
//    }
//}
//
//@Composable
//fun ContentScreen(
//    modifier: Modifier = Modifier,
//    navController: NavHostController,
//    selectedIndex: Int,
//    auth: FirebaseAuth // Thêm auth vào tham số
//) {
//    when (selectedIndex) {
//        0 -> HomeScreen(modifier = modifier, navController = navController)
//        1 -> MxhScreen(
//            modifier = modifier,
//            navController = navController
//        )
//        2 -> CategoryScreen(
//            auth = auth,  // Truyền FirebaseAuth
//            navController = navController
//        )
//        3 -> FavoriteScreen(
//            navController = navController,
//            viewModel = ProductViewModel(),
//            favoriteViewModel = FavoriteViewModel()
//        )
//        4 -> ProfileScreen(
//            auth = auth,  // Truyền FirebaseAuth
//            onSignIn = { navController.navigate("login_screen") }, // Điều hướng đến login khi đăng nhập
//            onSignOut = {
//                auth.signOut()  // Đăng xuất khỏi Firebase
//                navController.navigate("login_screen")  // Điều hướng về login
//            },
//            navController = navController
//        )
//    }
//}
