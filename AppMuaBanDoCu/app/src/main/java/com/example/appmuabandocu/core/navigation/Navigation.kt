//package com.example.appmuabandocu.core.navigation
//
//import android.content.Intent
//import android.util.Log
//import android.widget.Toast
//import androidx.compose.animation.AnimatedVisibility
//import androidx.compose.animation.core.Spring
//import androidx.compose.animation.core.animateFloatAsState
//import androidx.compose.animation.core.spring
//import androidx.compose.animation.fadeIn
//import androidx.compose.animation.fadeOut
//import androidx.compose.animation.expandVertically
//import androidx.compose.animation.shrinkVertically
//import androidx.compose.foundation.Image
//import androidx.compose.foundation.background
//import androidx.compose.foundation.border
//import androidx.compose.foundation.clickable
//import androidx.compose.foundation.layout.Arrangement
//import androidx.compose.foundation.layout.Box
//import androidx.compose.foundation.layout.Column
//import androidx.compose.foundation.layout.PaddingValues
//import androidx.compose.foundation.layout.Row
//import androidx.compose.foundation.layout.Spacer
//import androidx.compose.foundation.layout.aspectRatio
//import androidx.compose.foundation.layout.fillMaxHeight
//import androidx.compose.foundation.layout.fillMaxSize
//import androidx.compose.foundation.layout.fillMaxWidth
//import androidx.compose.foundation.layout.height
//import androidx.compose.foundation.layout.padding
//import androidx.compose.foundation.layout.size
//import androidx.compose.foundation.layout.statusBarsPadding
//import androidx.compose.foundation.layout.width
//import androidx.compose.foundation.layout.wrapContentHeight
//import androidx.compose.foundation.lazy.LazyColumn
//import androidx.compose.foundation.lazy.LazyRow
//import androidx.compose.foundation.lazy.grid.GridCells
//import androidx.compose.foundation.lazy.grid.GridItemSpan
//import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
//import androidx.compose.foundation.lazy.grid.items
//import androidx.compose.foundation.lazy.items
//import androidx.compose.foundation.lazy.rememberLazyListState
//import androidx.compose.foundation.shape.CircleShape
//import androidx.compose.foundation.shape.RoundedCornerShape
//import androidx.compose.foundation.text.BasicTextField
//import androidx.compose.material.BadgedBox
//import androidx.compose.material.ExperimentalMaterialApi
//import androidx.compose.material.Tab
//import androidx.compose.material.TabRow
//import androidx.compose.material.TabRowDefaults
//import androidx.compose.material.icons.Icons
//import androidx.compose.material.icons.filled.AccessTime
//import androidx.compose.material.icons.filled.Add
//import androidx.compose.material.icons.filled.Bookmark
//import androidx.compose.material.icons.filled.Close
//import androidx.compose.material.icons.filled.Comment
//import androidx.compose.material.icons.filled.Email
//import androidx.compose.material.icons.filled.Explore
//import androidx.compose.material.icons.filled.Favorite
//import androidx.compose.material.icons.filled.FavoriteBorder
//import androidx.compose.material.icons.filled.Flag
//import androidx.compose.material.icons.filled.Home
//import androidx.compose.material.icons.filled.LocationOn
//import androidx.compose.material.icons.filled.MoreVert
//import androidx.compose.material.icons.filled.Notifications
//import androidx.compose.material.icons.filled.Phone
//import androidx.compose.material.icons.filled.Search
//import androidx.compose.material.icons.filled.Visibility
//import androidx.compose.material.icons.filled.VisibilityOff
//import androidx.compose.material.pullrefresh.PullRefreshIndicator
//import androidx.compose.material.pullrefresh.pullRefresh
//import androidx.compose.material.pullrefresh.rememberPullRefreshState
//import androidx.compose.material3.Badge
//import androidx.compose.material3.Button
//import androidx.compose.material3.ButtonDefaults
//import androidx.compose.material3.Card
//import androidx.compose.material3.CardDefaults
//import androidx.compose.material3.Divider
//import androidx.compose.material3.DropdownMenu
//import androidx.compose.material3.DropdownMenuItem
//import androidx.compose.material3.ExperimentalMaterial3Api
//import androidx.compose.material3.FloatingActionButton
//import androidx.compose.material3.Icon
//import androidx.compose.material3.IconButton
//import androidx.compose.material3.OutlinedTextField
//import androidx.compose.material3.Scaffold
//import androidx.compose.material3.Surface
//import androidx.compose.material3.Text
//import androidx.compose.material3.TextButton
//import androidx.compose.material3.TextFieldDefaults
//import androidx.compose.runtime.Composable
//import androidx.compose.runtime.LaunchedEffect
//import androidx.compose.runtime.collectAsState
//import androidx.compose.runtime.getValue
//import androidx.compose.runtime.mutableStateOf
//import androidx.compose.runtime.remember
//import androidx.compose.runtime.setValue
//import androidx.compose.ui.Alignment
//import androidx.compose.ui.Modifier
//import androidx.compose.ui.draw.clip
//import androidx.compose.ui.draw.scale
//import androidx.compose.ui.draw.shadow
//import androidx.compose.ui.graphics.Brush
//import androidx.compose.ui.graphics.Color
//import androidx.compose.ui.graphics.SolidColor
//import androidx.compose.ui.layout.ContentScale
//import androidx.compose.ui.platform.LocalContext
//import androidx.compose.ui.res.painterResource
//import androidx.compose.ui.text.SpanStyle
//import androidx.compose.ui.text.TextStyle
//import androidx.compose.ui.text.buildAnnotatedString
//import androidx.compose.ui.text.font.FontWeight
//import androidx.compose.ui.text.style.TextAlign
//import androidx.compose.ui.text.style.TextOverflow
//import androidx.compose.ui.text.withStyle
//import androidx.compose.ui.unit.dp
//import androidx.compose.ui.unit.sp
//import androidx.core.net.toUri
//import androidx.lifecycle.viewmodel.compose.viewModel
//import androidx.navigation.NavController
//import coil.compose.AsyncImage
//import com.example.appmuabandocu.R
//import com.example.appmuabandocu.model.Product
//import com.example.appmuabandocu.ui.theme.Background_Light
//import com.example.appmuabandocu.ui.theme.Blue_text
//import com.example.appmuabandocu.ui.theme.orange
//import com.example.appmuabandocu.viewmodel.ProductViewModel
//import com.example.appmuabandocu.viewmodel.SearchProductViewModel
//import java.text.DecimalFormat
//import java.text.NumberFormat
//import java.text.SimpleDateFormat
//import java.util.Date
//import java.util.Locale
//import androidx.compose.foundation.lazy.grid.LazyGridItemScope
//import androidx.compose.material.TabRowDefaults.tabIndicatorOffset
//import androidx.compose.ui.tooling.preview.Preview
//import androidx.navigation.compose.rememberNavController
//
//@OptIn(ExperimentalMaterialApi::class, ExperimentalMaterial3Api::class)
//@Composable
//fun MxhScreen1(
//   modifier: Modifier = Modifier,
//   navController: NavController,
//   productViewModel: ProductViewModel = viewModel(),
//   searchViewModel: SearchProductViewModel = viewModel(),
//) {
//   // Màu sắc chính
//   val Blue_text = Color(0xFF31ACE6)
//   val orange = Color(0xFFFFB74D)
//   val Background_Light = Color(0xFFF8FCFF)
//
//   // Trạng thái tìm kiếm
//   var query by remember { mutableStateOf("") }
//   val searchResults by searchViewModel.searchResults.collectAsState<List<Product>>()
//   val sortedSearchResults = remember(searchResults) {
//       searchResults.sortedByDescending { it.timestamp }
//   }
//
//   // Trạng thái refresh
//   val isRefreshing by productViewModel.isRefreshing.collectAsState()
//   val pullRefreshState = rememberPullRefreshState(
//       refreshing = isRefreshing,
//       onRefresh = { productViewModel.refreshProducts() }
//   )
//
//   // Trạng thái tab
//   var selectedTab by remember { mutableStateOf(0) }
//   val tabs = listOf("Trang chủ", "Khám phá", "Thông báo")
//
//   // Trạng thái tìm kiếm
//   var showSearch by remember { mutableStateOf(false) }
//
//   Surface(
//       modifier = Modifier.fillMaxSize(),
//       color = Color(0xFFF5F5F5) // Màu nền giống Facebook/Instagram
//   ) {
//       Scaffold(
//           topBar = {
//               Surface(
//                   modifier = Modifier
//                       .fillMaxWidth()
//                       .statusBarsPadding(),
//                   color = Color.White,
//                   shadowElevation = 2.dp
//               ) {
//                   Column {
//                       // Header chính
//                       Row(
//                           modifier = Modifier
//                               .fillMaxWidth()
//                               .padding(horizontal = 16.dp, vertical = 12.dp),
//                           verticalAlignment = Alignment.CenterVertically
//                       ) {
//                           // Logo
//                           Text(
//                               text = "Thanh Lý",
//                               fontSize = 24.sp,
//                               fontWeight = FontWeight.Bold,
//                               color = orange,
//                               modifier = Modifier.weight(1f)
//                           )
//
//                           // Các icon hành động
//                           IconButton(
//                               onClick = { showSearch = !showSearch },
//                               modifier = Modifier.size(40.dp)
//                           ) {
//                               Icon(
//                                   imageVector = Icons.Default.Search,
//                                   contentDescription = "Tìm kiếm",
//                                   tint = Color.DarkGray
//                               )
//                           }
//
//                           IconButton(
//                               onClick = { /* TODO: Show notifications */ },
//                               modifier = Modifier.size(40.dp)
//                           ) {
//                               BadgedBox(
//                                   badge = {
//                                       Badge {
//                                           Text("3")
//                                       }
//                                   }
//                               ) {
//                                   Icon(
//                                       imageVector = Icons.Default.Notifications,
//                                       contentDescription = "Thông báo",
//                                       tint = Color.DarkGray
//                                   )
//                               }
//                           }
//
//                           IconButton(
//                               onClick = { /* TODO: Show messages */ },
//                               modifier = Modifier.size(40.dp)
//                           ) {
//                               BadgedBox(
//                                   badge = {
//                                       Badge {
//                                           Text("2")
//                                       }
//                                   }
//                               ) {
//                                   Icon(
//                                       imageVector = Icons.Default.Email,
//                                       contentDescription = "Tin nhắn",
//                                       tint = Color.DarkGray
//                                   )
//                               }
//                           }
//                       }
//
//                       // Thanh tìm kiếm (hiển thị khi cần)
//                       AnimatedVisibility(
//                           visible = showSearch,
//                           enter = expandVertically() + fadeIn(),
//                           exit = shrinkVertically() + fadeOut()
//                       ) {
//                           Card(
//                               modifier = Modifier
//                                   .fillMaxWidth()
//                                   .height(56.dp)
//                                   .padding(horizontal = 16.dp, vertical = 8.dp),
//                               shape = RoundedCornerShape(28.dp),
//                               colors = CardDefaults.cardColors(
//                                   containerColor = Color(0xFFEEEEEE)
//                               ),
//                               elevation = CardDefaults.cardElevation(
//                                   defaultElevation = 0.dp
//                               )
//                           ) {
//                               Row(
//                                   modifier = Modifier
//                                       .fillMaxSize()
//                                       .padding(horizontal = 16.dp),
//                                   verticalAlignment = Alignment.CenterVertically
//                               ) {
//                                   Icon(
//                                       imageVector = Icons.Default.Search,
//                                       contentDescription = "Search",
//                                       tint = Color.Gray,
//                                       modifier = Modifier.size(20.dp)
//                                   )
//
//                                   Spacer(modifier = Modifier.width(8.dp))
//
//                                   BasicTextField(
//                                       value = query,
//                                       onValueChange = {
//                                           query = it
//                                           productViewModel.searchProducts(it)
//                                       },
//                                       modifier = Modifier
//                                           .weight(1f)
//                                           .fillMaxHeight(),
//                                       textStyle = TextStyle(
//                                           fontSize = 16.sp,
//                                           color = Color.Black
//                                       ),
//                                       singleLine = true,
//                                       cursorBrush = SolidColor(Blue_text),
//                                       decorationBox = { innerTextField ->
//                                           Box(
//                                               modifier = Modifier.fillMaxSize(),
//                                               contentAlignment = Alignment.CenterStart
//                                           ) {
//                                               if (query.isEmpty()) {
//                                                   Text(
//                                                       text = "Tìm kiếm sản phẩm...",
//                                                       fontSize = 16.sp,
//                                                       color = Color.Gray
//                                                   )
//                                               }
//                                               innerTextField()
//                                           }
//                                       }
//                                   )
//
//                                   if (query.isNotEmpty()) {
//                                       IconButton(
//                                           onClick = {
//                                               query = ""
//                                               productViewModel.searchProducts("")
//                                           },
//                                           modifier = Modifier.size(20.dp)
//                                       ) {
//                                           Icon(
//                                               imageVector = Icons.Default.Close,
//                                               contentDescription = "Clear",
//                                               tint = Color.Gray,
//                                               modifier = Modifier.size(16.dp)
//                                           )
//                                       }
//                                   }
//                               }
//                           }
//                       }
//
//                       // Tab Navigation
//                       TabRow(
//                           selectedTabIndex = selectedTab,
//                           backgroundColor = Color.White,
//                           contentColor = orange,
//                           indicator = { tabPositions ->
//                               TabRowDefaults.Indicator(
//                                   modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
//                                   height = 3.dp,
//                                   color = orange
//                               )
//                           }
//                       ) {
//                           tabs.forEachIndexed { index, title ->
//                               Tab(
//                                   selected = selectedTab == index,
//                                   onClick = { selectedTab = index },
//                                   text = {
//                                       Text(
//                                           text = title,
//                                           fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Normal
//                                       )
//                                   },
//                                   icon = {
//                                       when (index) {
//                                           0 -> Icon(Icons.Default.Home, contentDescription = null)
//                                           1 -> Icon(Icons.Default.Explore, contentDescription = null)
//                                           2 -> Icon(Icons.Default.Notifications, contentDescription = null)
//                                       }
//                                   }
//                               )
//                           }
//                       }
//                   }
//               }
//           },
//           bottomBar = { BottomNavBar(navController = navController) },
//           floatingActionButton = {
//               FloatingActionButton(
//                   onClick = { navController.navigate("add_product") },
//                   containerColor = orange,
//                   contentColor = Color.White
//               ) {
//                   Icon(
//                       imageVector = Icons.Default.Add,
//                       contentDescription = "Đăng bài mới"
//                   )
//               }
//           },
//           containerColor = Color(0xFFF5F5F5)
//       ) { paddingValues ->
//           Box(
//               modifier = Modifier
//                   .fillMaxSize()
//                   .padding(paddingValues)
//                   .pullRefresh(pullRefreshState)
//           ) {
//               when (selectedTab) {
//                   0 -> {
//                       // Tab Trang chủ
//                       Column {
//                           // Stories/Highlights
//                           StoriesSection()
//
//                           // Danh sách bài đăng
//                           if (sortedSearchResults.isEmpty() && query.isEmpty() && !isRefreshing) {
//                               // Trạng thái trống
//                               EmptyFeedState()
//                           } else {
//                               // Danh sách bài đăng
//                               LazyColumn(
//                                   contentPadding = PaddingValues(bottom = 16.dp),
//                                   verticalArrangement = Arrangement.spacedBy(8.dp)
//                               ) {
//                                   item {
//                                       Spacer(modifier = Modifier.height(8.dp))
//                                   }
//
//                                   if (query.isNotEmpty()) {
//                                       item {
//                                           Text(
//                                               text = "Kết quả tìm kiếm: ${sortedSearchResults.size} sản phẩm",
//                                               modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp),
//                                               fontSize = 14.sp,
//                                               fontWeight = FontWeight.Medium,
//                                               color = Color.Gray
//                                           )
//                                       }
//                                   }
//
//                                   items(
//                                       items = sortedSearchResults,
//                                       key = { it.id }
//                                   ) { product ->
//                                       SocialMediaPostItem(
//                                           product = product,
//                                           navController = navController,
//                                           toggleProductVisibility = {
//                                               productViewModel.toggleProductVisibility(product.id)
//                                           }
//                                       )
//                                   }
//
//                                   item {
//                                       Spacer(modifier = Modifier.height(80.dp))
//                                   }
//                               }
//                           }
//                       }
//                   }
//                   1 -> {
//                       // Tab Khám phá - Hiển thị dạng lưới
//                       ExploreGridView(sortedSearchResults, navController)
//                   }
//                   2 -> {
//                       // Tab Thông báo
//                       NotificationsView()
//                   }
//               }
//
//               // Pull-to-refresh indicator
//               PullRefreshIndicator(
//                   refreshing = isRefreshing,
//                   state = pullRefreshState,
//                   modifier = Modifier.align(Alignment.TopCenter),
//                   backgroundColor = orange,
//                   contentColor = Color.White
//               )
//           }
//       }
//   }
//}
//
//@Composable
//fun StoriesSection() {
//   // Danh sách stories giả
//   val stories = listOf(
//       "Của bạn" to "https://randomuser.me/api/portraits/men/32.jpg",
//       "Nguyễn A" to "https://randomuser.me/api/portraits/women/44.jpg",
//       "Trần B" to "https://randomuser.me/api/portraits/men/46.jpg",
//       "Lê C" to "https://randomuser.me/api/portraits/women/65.jpg",
//       "Phạm D" to "https://randomuser.me/api/portraits/men/29.jpg",
//       "Hoàng E" to "https://randomuser.me/api/portraits/women/21.jpg",
//       "Đặng F" to "https://randomuser.me/api/portraits/men/18.jpg"
//   )
//
//   Card(
//       modifier = Modifier
//           .fillMaxWidth()
//           .padding(vertical = 8.dp),
//       colors = CardDefaults.cardColors(containerColor = Color.White),
//       elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
//   ) {
//       Column(modifier = Modifier.padding(vertical = 8.dp)) {
//           Text(
//               text = "Stories",
//               fontWeight = FontWeight.Bold,
//               fontSize = 16.sp,
//               modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//           )
//
//           LazyRow(
//               contentPadding = PaddingValues(horizontal = 16.dp),
//               horizontalArrangement = Arrangement.spacedBy(12.dp)
//           ) {
//               items(stories) { (name, avatar) ->
//                   StoryItem(name = name, avatarUrl = avatar, isYours = name == "Của bạn")
//               }
//           }
//       }
//   }
//}
//
//@Composable
//fun StoryItem(name: String, avatarUrl: String, isYours: Boolean = false) {
//   val orange = Color(0xFFFFB74D)
//
//   Column(
//       horizontalAlignment = Alignment.CenterHorizontally,
//       modifier = Modifier.width(72.dp)
//   ) {
//       Box(contentAlignment = Alignment.Center) {
//           // Vòng tròn gradient bên ngoài
//           Box(
//               modifier = Modifier
//                   .size(64.dp)
//                   .background(
//                       brush = Brush.linearGradient(
//                           colors = if (isYours)
//                               listOf(Color.LightGray, Color.Gray)
//                           else
//                               listOf(orange, Color(0xFFFF7043), Color(0xFFE64A19))
//                       ),
//                       shape = CircleShape
//                   )
//           )
//
//           // Avatar
//           AsyncImage(
//               model = avatarUrl.replace("http://", "https://"),
//               contentDescription = "Avatar",
//               modifier = Modifier
//                   .size(60.dp)
//                   .clip(CircleShape)
//                   .border(width = 2.dp, color = Color.White, shape = CircleShape),
//               contentScale = ContentScale.Crop
//           )
//
//           // Nút thêm cho story của bạn
//           if (isYours) {
//               Box(
//                   modifier = Modifier
//                       .align(Alignment.BottomEnd)
//                       .size(20.dp)
//                       .background(orange, CircleShape)
//                       .border(2.dp, Color.White, CircleShape),
//                   contentAlignment = Alignment.Center
//               ) {
//                   Icon(
//                       imageVector = Icons.Default.Add,
//                       contentDescription = "Thêm story",
//                       tint = Color.White,
//                       modifier = Modifier.size(12.dp)
//                   )
//               }
//           }
//       }
//
//       Spacer(modifier = Modifier.height(4.dp))
//
//       Text(
//           text = name,
//           fontSize = 12.sp,
//           color = Color.DarkGray,
//           maxLines = 1,
//           overflow = TextOverflow.Ellipsis,
//           textAlign = TextAlign.Center
//       )
//   }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun SocialMediaPostItem(
//   product: Product,
//   navController: NavController,
//   toggleProductVisibility: () -> Unit
//) {
//   val context = LocalContext.current
//
//   // Định dạng thời gian
//   val formattedTime = remember(product.timestamp) {
//       val date = Date(product.timestamp)
//       val now = Date()
//       val diffInMillis = now.time - date.time
//       val diffInMinutes = diffInMillis / (60 * 1000)
//       val diffInHours = diffInMillis / (60 * 60 * 1000)
//       val diffInDays = diffInMillis / (24 * 60 * 60 * 1000)
//
//       when {
//           diffInMinutes < 60 -> "$diffInMinutes phút trước"
//           diffInHours < 24 -> "$diffInHours giờ trước"
//           diffInDays < 7 -> "$diffInDays ngày trước"
//           else -> {
//               val formatter = SimpleDateFormat("dd/MM/yyyy", Locale.getDefault())
//               formatter.format(date)
//           }
//       }
//   }
//
//   // Trạng thái menu
//   var expanded by remember { mutableStateOf(false) }
//
//   // Trạng thái tương tác
//   var isLiked by remember { mutableStateOf(false) }
//   var likeCount by remember { mutableStateOf((10..50).random()) }
//   var commentCount by remember { mutableStateOf((0..20).random()) }
//
//   // Animation cho nút like
//   val scale by animateFloatAsState(
//       targetValue = if (isLiked) 1.2f else 1f,
//       animationSpec = spring(
//           dampingRatio = Spring.DampingRatioMediumBouncy,
//           stiffness = Spring.StiffnessLow
//       ),
//       label = "like_scale"
//   )
//
//   Card(
//       modifier = Modifier
//           .fillMaxWidth(),
//       colors = CardDefaults.cardColors(containerColor = Color.White),
//       elevation = CardDefaults.cardElevation(defaultElevation = 1.dp)
//   ) {
//       Column {
//           // Header - Thông tin người đăng
//           Row(
//               modifier = Modifier
//                   .fillMaxWidth()
//                   .padding(16.dp),
//               verticalAlignment = Alignment.CenterVertically
//           ) {
//               // Avatar người đăng
//               AsyncImage(
//                   model = product.userAvatar.replace("http://", "https://"),
//                   contentDescription = "Avatar",
//                   modifier = Modifier
//                       .size(40.dp)
//                       .clip(CircleShape)
//                       .border(1.dp, Color.LightGray, CircleShape),
//                   contentScale = ContentScale.Crop,
//                   placeholder = painterResource(id = R.drawable.placeholders_product),
//                   error = painterResource(id = R.drawable.error)
//               )
//
//               Spacer(modifier = Modifier.width(12.dp))
//
//               // Thông tin người đăng và thời gian
//               Column(
//                   modifier = Modifier.weight(1f)
//               ) {
//                   Text(
//                       text = product.userName,
//                       fontWeight = FontWeight.Bold,
//                       fontSize = 14.sp
//                   )
//
//                   Row(
//                       verticalAlignment = Alignment.CenterVertically
//                   ) {
//                       Text(
//                           text = formattedTime,
//                           fontSize = 12.sp,
//                           color = Color.Gray
//                       )
//
//                       Text(
//                           text = " • ${product.provinceFB}",
//                           fontSize = 12.sp,
//                           color = Color.Gray
//                       )
//
//                       // Trạng thái sản phẩm
//                       product.status?.let { status ->
//                           val statusText = when(status) {
//                               "sold" -> "Đã bán"
//                               "available" -> "Còn hàng"
//                               else -> "Đã ẩn"
//                           }
//                           Text(
//                               text = " • $statusText",
//                               fontSize = 12.sp,
//                               color = when(status) {
//                                   "sold" -> Color.Red
//                                   "available" -> Color(0xFF4CAF50)
//                                   else -> Color.Gray
//                               }
//                           )
//                       }
//                   }
//               }
//
//               // Menu
//               IconButton(onClick = { expanded = true }) {
//                   Icon(
//                       imageVector = Icons.Default.MoreVert,
//                       contentDescription = "More",
//                       tint = Color.Gray
//                   )
//               }
//
//               DropdownMenu(
//                   expanded = expanded,
//                   onDismissRequest = { expanded = false }
//               ) {
//                   DropdownMenuItem(
//                       text = { Text("Lưu bài viết") },
//                       onClick = {
//                           expanded = false
//                           Toast.makeText(context, "Đã lưu bài viết", Toast.LENGTH_SHORT).show()
//                       },
//                       leadingIcon = {
//                           Icon(
//                               imageVector = Icons.Default.Bookmark,
//                               contentDescription = null
//                           )
//                       }
//                   )
//                   DropdownMenuItem(
//                       text = { Text("Báo cáo bài viết") },
//                       onClick = {
//                           expanded = false
//                           Toast.makeText(context, "Đã báo cáo bài viết", Toast.LENGTH_SHORT).show()
//                       },
//                       leadingIcon = {
//                           Icon(
//                               imageVector = Icons.Default.Flag,
//                               contentDescription = null
//                           )
//                       }
//                   )
//                   DropdownMenuItem(
//                       text = { Text("Ẩn bài viết") },
//                       onClick = {
//                           expanded = false
//                           toggleProductVisibility()
//                       },
//                       leadingIcon = {
//                           Icon(
//                               imageVector = Icons.Default.VisibilityOff,
//                               contentDescription = null
//                           )
//                       }
//                   )
//               }
//           }
//
//           // Mô tả sản phẩm (nếu có)
//           product.details?.let {
//               if (it.isNotEmpty()) {
//                   Text(
//                       text = it,
//                       fontSize = 14.sp,
//                       color = Color.DarkGray,
//                       modifier = Modifier.padding(horizontal = 16.dp, vertical = 4.dp)
//                   )
//               }
//           }
//
//           // Hình ảnh sản phẩm
//           AsyncImage(
//               model = product.imageUrl.replace("http://", "https://"),
//               contentDescription = "Hình sản phẩm",
//               modifier = Modifier
//                   .fillMaxWidth()
//                   .height(300.dp)
//                   .clickable { navController.navigate("product_detail/${product.id}") },
//               contentScale = ContentScale.Crop,
//               placeholder = painterResource(id = R.drawable.placeholders_product),
//               error = painterResource(id = R.drawable.error)
//           )
//
//           // Thông tin sản phẩm
//           Column(
//               modifier = Modifier.padding(16.dp)
//           ) {
//               // Tên và giá sản phẩm
//               Row(
//                   modifier = Modifier.fillMaxWidth(),
//                   horizontalArrangement = Arrangement.SpaceBetween,
//                   verticalAlignment = Alignment.CenterVertically
//               ) {
//                   Text(
//                       text = product.productName,
//                       fontWeight = FontWeight.Bold,
//                       fontSize = 16.sp,
//                       color = Color.Black
//                   )
//
//                   Text(
//                       text = formatPrice(product.price.toDouble()),
//                       color = Blue_text,
//                       fontSize = 16.sp,
//                       fontWeight = FontWeight.Bold
//                   )
//               }
//
//               Spacer(modifier = Modifier.height(12.dp))
//
//               // Thanh tương tác
//               Row(
//                   modifier = Modifier.fillMaxWidth(),
//                   horizontalArrangement = Arrangement.SpaceBetween
//               ) {
//                   // Thông tin lượt thích và bình luận
//                   Row(
//                       verticalAlignment = Alignment.CenterVertically
//                   ) {
//                       Icon(
//                           imageVector = Icons.Default.Favorite,
//                           contentDescription = null,
//                           tint = orange,
//                           modifier = Modifier.size(16.dp)
//                       )
//                       Spacer(modifier = Modifier.width(4.dp))
//                       Text(
//                           text = "$likeCount",
//                           fontSize = 14.sp,
//                           color = Color.Gray
//                       )
//
//                       Spacer(modifier = Modifier.width(16.dp))
//
//                       Icon(
//                           imageVector = Icons.Default.Comment,
//                           contentDescription = null,
//                           tint = Color.Gray,
//                           modifier = Modifier.size(16.dp)
//                       )
//                       Spacer(modifier = Modifier.width(4.dp))
//                       Text(
//                           text = "$commentCount",
//                           fontSize = 14.sp,
//                           color = Color.Gray
//                       )
//                   }
//
//                   // Lượt xem
//                   Row(
//                       verticalAlignment = Alignment.CenterVertically
//                   ) {
//                       Icon(
//                           imageVector = Icons.Default.Visibility,
//                           contentDescription = null,
//                           tint = Color.Gray,
//                           modifier = Modifier.size(16.dp)
//                       )
//                       Spacer(modifier = Modifier.width(4.dp))
//                       Text(
//                           text = "${(100..999).random()}",
//                           fontSize = 14.sp,
//                           color = Color.Gray
//                       )
//                   }
//               }
//
//               Divider(
//                   modifier = Modifier.padding(vertical = 12.dp),
//                   color = Color.LightGray
//               )
//
//               // Các nút tương tác
//               Row(
//                   modifier = Modifier.fillMaxWidth(),
//                   horizontalArrangement = Arrangement.SpaceEvenly
//               ) {
//                   // Nút thích
//                   Row(
//                       modifier = Modifier
//                           .clickable {
//                               isLiked = !isLiked
//                               likeCount += if (isLiked) 1 else -1
//                           }
//                           .padding(vertical = 8.dp),
//                       verticalAlignment = Alignment.CenterVertically,
//                       horizontalArrangement = Arrangement.Center
//                   ) {
//                       Icon(
//                           imageVector = if (isLiked) Icons.Filled.Favorite else Icons.Default.FavoriteBorder,
//                           contentDescription = "Thích",
//                           tint = if (isLiked) orange else Color.Gray,
//                           modifier = Modifier
//                               .size(20.dp)
//                               .scale(scale)
//                       )
//                       Spacer(modifier = Modifier.width(4.dp))
//                       Text(
//                           text = "Thích",
//                           color = if (isLiked) orange else Color.Gray
//                       )
//                   }
//
//                   // Nút bình luận
//                   Row(
//                       modifier = Modifier
//                           .clickable { /* TODO: Show comments */ }
//                           .padding(vertical = 8.dp),
//                       verticalAlignment = Alignment.CenterVertically,
//                       horizontalArrangement = Arrangement.Center
//                   ) {
//                       Icon(
//                           imageVector = Icons.Default.Comment,
//                           contentDescription = "Bình luận",
//                           tint = Color.Gray,
//                           modifier = Modifier.size(20.dp)
//                       )
//                       Spacer(modifier = Modifier.width(4.dp))
//                       Text(
//                           text = "Bình luận",
//                           color = Color.Gray
//                       )
//                   }
//
//                   // Nút liên hệ
//                   Row(
//                       modifier = Modifier
//                           .clickable {
//                               val intent = Intent(Intent.ACTION_DIAL).apply {
//                                   data = "tel:${product.numberUser}".toUri()
//                               }
//                               context.startActivity(intent)
//                           }
//                           .padding(vertical = 8.dp),
//                       verticalAlignment = Alignment.CenterVertically,
//                       horizontalArrangement = Arrangement.Center
//                   ) {
//                       Icon(
//                           imageVector = Icons.Default.Phone,
//                           contentDescription = "Liên hệ",
//                           tint = Blue_text,
//                           modifier = Modifier.size(20.dp)
//                       )
//                       Spacer(modifier = Modifier.width(4.dp))
//                       Text(
//                           text = "Liên hệ",
//                           color = Blue_text
//                       )
//                   }
//               }
//           }
//       }
//   }
//}
//
//@Composable
//fun ExploreGridView(products: List<Product>, navController: NavController) {
//   if (products.isEmpty()) {
//       EmptyFeedState()
//   } else {
//       LazyVerticalGrid(
//           columns = GridCells.Fixed(2),
//           contentPadding = PaddingValues(8.dp),
//           horizontalArrangement = Arrangement.spacedBy(8.dp),
//           verticalArrangement = Arrangement.spacedBy(8.dp)
//       ) {
//           items(products) { product ->
//               ExploreGridItem(product, navController)
//           }
//
//           item(span = { GridItemSpan(2) }) {
//               Spacer(modifier = Modifier.height(80.dp))
//           }
//       }
//   }
//}
//
//@OptIn(ExperimentalMaterial3Api::class)
//@Composable
//fun ExploreGridItem(product: Product, navController: NavController) {
//   val Blue_text = Color(0xFF31ACE6)
//
//   Card(
//       modifier = Modifier
//           .fillMaxWidth()
//           .aspectRatio(0.8f),
//       shape = RoundedCornerShape(8.dp),
//       colors = CardDefaults.cardColors(containerColor = Color.White),
//       elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
//       onClick = { navController.navigate("product_detail/${product.id}") }
//   ) {
//       Column {
//           // Hình ảnh sản phẩm
//           Box(
//               modifier = Modifier
//                   .fillMaxWidth()
//                   .weight(1f)
//           ) {
//               AsyncImage(
//                   model = product.imageUrl.replace("http://", "https://"),
//                   contentDescription = "Hình sản phẩm",
//                   modifier = Modifier.fillMaxSize(),
//                   contentScale = ContentScale.Crop,
//                   placeholder = painterResource(id = R.drawable.placeholders_product),
//                   error = painterResource(id = R.drawable.error)
//               )
//
//               // Trạng thái sản phẩm
//               product.status?.let { status ->
//                   if (status == "sold") {
//                       Box(
//                           modifier = Modifier
//                               .fillMaxSize()
//                               .background(Color.Black.copy(alpha = 0.5f)),
//                           contentAlignment = Alignment.Center
//                       ) {
//                           Text(
//                               text = "ĐÃ BÁN",
//                               color = Color.White,
//                               fontWeight = FontWeight.Bold,
//                               fontSize = 16.sp
//                           )
//                       }
//                   }
//               }
//           }
//
//           // Thông tin sản phẩm
//           Column(
//               modifier = Modifier.padding(8.dp)
//           ) {
//               Text(
//                   text = product.productName,
//                   fontWeight = FontWeight.Medium,
//                   fontSize = 14.sp,
//                   maxLines = 1,
//                   overflow = TextOverflow.Ellipsis
//               )
//
//               Spacer(modifier = Modifier.height(4.dp))
//
//               Text(
//                   text = formatPrice(product.price.toDouble()),
//                   color = Blue_text,
//                   fontSize = 14.sp,
//                   fontWeight = FontWeight.Bold
//               )
//           }
//       }
//   }
//}
//
//@Composable
//fun NotificationsView() {
//   val notifications = listOf(
//       Triple("Nguyễn Văn A đã thích sản phẩm của bạn", "2 giờ trước", "https://randomuser.me/api/portraits/men/32.jpg"),
//       Triple("Trần Thị B đã bình luận về sản phẩm của bạn", "5 giờ trước", "https://randomuser.me/api/portraits/women/44.jpg"),
//       Triple("Lê Văn C đã gửi tin nhắn cho bạn", "1 ngày trước", "https://randomuser.me/api/portraits/men/46.jpg"),
//       Triple("Phạm Thị D đã theo dõi bạn", "2 ngày trước", "https://randomuser.me/api/portraits/women/65.jpg"),
//       Triple("Hoàng Văn E đã đăng sản phẩm mới", "3 ngày trước", "https://randomuser.me/api/portraits/men/29.jpg")
//   )
//
//   LazyColumn(
//       contentPadding = PaddingValues(vertical = 8.dp)
//   ) {
//       item {
//           Text(
//               text = "Thông báo",
//               fontWeight = FontWeight.Bold,
//               fontSize = 18.sp,
//               modifier = Modifier.padding(horizontal = 16.dp, vertical = 8.dp)
//           )
//       }
//
//       items(notifications) { (message, time, avatar) ->
//           NotificationItem(message, time, avatar)
//       }
//
//       item {
//           Spacer(modifier = Modifier.height(80.dp))
//       }
//   }
//}
//
//@Composable
//fun NotificationItem(message: String, time: String, avatar: String) {
//   Row(
//       modifier = Modifier
//           .fillMaxWidth()
//           .padding(horizontal = 16.dp, vertical = 12.dp),
//       verticalAlignment = Alignment.CenterVertically
//   ) {
//       // Avatar
//       AsyncImage(
//           model = avatar,
//           contentDescription = "Avatar",
//           modifier = Modifier
//               .size(48.dp)
//               .clip(CircleShape),
//           contentScale = ContentScale.Crop
//       )
//
//       Spacer(modifier = Modifier.width(12.dp))
//
//       // Nội dung thông báo
//       Column(
//           modifier = Modifier.weight(1f)
//       ) {
//           Text(
//               text = message,
//               fontSize = 14.sp
//           )
//
//           Spacer(modifier = Modifier.height(4.dp))
//
//           Text(
//               text = time,
//               fontSize = 12.sp,
//               color = Color.Gray
//           )
//       }
//   }
//
//   Divider(
//       modifier = Modifier.padding(start = 76.dp),
//       color = Color.LightGray.copy(alpha = 0.5f)
//   )
//}
//
//@Composable
//fun EmptyFeedState() {
//   Box(
//       modifier = Modifier
//           .fillMaxSize()
//           .padding(16.dp),
//       contentAlignment = Alignment.Center
//   ) {
//       Column(
//           horizontalAlignment = Alignment.CenterHorizontally,
//           verticalArrangement = Arrangement.Center
//       ) {
//           Image(
//               painter = painterResource(id = R.drawable.ic_empty_task),
//               contentDescription = "Không có bài viết",
//               modifier = Modifier
//                   .size(160.dp)
//                   .padding(bottom = 24.dp)
//           )
//
//           Text(
//               text = "Chưa có bài viết nào",
//               fontSize = 20.sp,
//               fontWeight = FontWeight.Bold,
//               color = Color.DarkGray
//           )
//
//           Spacer(modifier = Modifier.height(8.dp))
//
//           Text(
//               text = "Hãy theo dõi nhiều người hơn hoặc đăng bài viết mới",
//               fontSize = 16.sp,
//               color = Color.Gray,
//               textAlign = TextAlign.Center
//           )
//       }
//   }
//}
//
//// Hàm định dạng giá tiền
//fun formatPrice(price: Double): String {
//   val formatter = DecimalFormat("#,###")
//   return "${formatter.format(price)} VNĐ"
//}
//
//@Preview(showBackground = true, showSystemUi = true)
//@Composable
//fun MxhScreenPreview() {
//    val navController = rememberNavController()
//    MxhScreen1(navController = navController)
//}