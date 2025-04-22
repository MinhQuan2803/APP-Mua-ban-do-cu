package com.example.appmuabandocu.feature_product.ui

import ProductViewModel
import android.content.Intent
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Favorite
import androidx.compose.material.icons.filled.FavoriteBorder
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import androidx.navigation.compose.rememberNavController
import coil.compose.AsyncImage
import coil.compose.rememberAsyncImagePainter
import com.example.appmuabandocu.ui.theme.Blue_text
import com.example.appmuabandocu.R
import com.example.appmuabandocu.data.Product
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.window.Dialog
import com.example.appmuabandocu.feature_mxh.ui.formatPrice
import com.example.appmuabandocu.viewmodel.FavoriteViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import androidx.core.net.toUri
import androidx.compose.foundation.gestures.detectTransformGestures
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.offset
import androidx.compose.runtime.mutableStateOf
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.runtime.*
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.IntOffset
import kotlin.math.roundToInt
@Composable
fun ProductDetailScreen(
    navController: NavController,
    id: String,
    viewModel: ProductViewModel = viewModel(),
    favoriteViewModel: FavoriteViewModel = viewModel(),
    product: Product? = null
) {
    val product = viewModel.productList.find { it.id == id }
    val context = LocalContext.current
    val phoneNumber = product?.numberUser
    val favoriteIds = favoriteViewModel.favoriteProductIds.collectAsState()
    val formattedTime = remember(product?.timestamp) {
        val date = Date(product?.timestamp ?: 0)
        val formatter = SimpleDateFormat("HH:mm dd/MM/yyyy", Locale.getDefault())
        formatter.format(date)
    }

    // State để kiểm soát dialog hình ảnh
    var showDialog by remember { mutableStateOf(false) }
    var selectedImageUrl by remember { mutableStateOf("") }

    if (product == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Không tìm thấy sản phẩm", color = Color.Red)
        }
    } else {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White),
            contentAlignment = Alignment.TopCenter
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .widthIn(max = 600.dp) // giới hạn chiều rộng
            ) {
                // Header
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(92.dp)
                        .background(Blue_text),
                    contentAlignment = Alignment.Center
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .align(Alignment.BottomCenter),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        IconButton(onClick = { navController.popBackStack() }) {
                            Icon(
                                imageVector = Icons.Default.KeyboardArrowLeft,
                                contentDescription = "Back",
                                modifier = Modifier.size(40.dp),
                                tint = Color.Black
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text("Yêu thích")
                        IconButton(onClick = { favoriteViewModel.toggleFavorite(product.id) }) {
                            Icon(
                                imageVector = if (favoriteIds.value.contains(product.id))
                                    Icons.Default.Favorite else
                                    Icons.Default.FavoriteBorder,
                                contentDescription = "Favorite"
                            )
                        }
                    }
                }

                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(rememberScrollState())
                        .padding(bottom = 30.dp)
                ) {
                    // Ảnh sản phẩm
                    AsyncImage(
                        model = product.imageUrl.replace("http://", "https://"),
                        contentDescription = "Hình sản phẩm",
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .fillMaxWidth()
                            .aspectRatio(1.5f)
                            .clickable {
                                selectedImageUrl = product.imageUrl.replace("http://", "https://")
                                showDialog = true
                            },
                        placeholder = painterResource(id = R.drawable.ic_noicom),
                        error = painterResource(id = R.drawable.ic_xemay)
                    )

                    // Hiển thị dialog nếu showDialog là true
                    if (showDialog) {
                        FullScreenImageDialog(imageUrl = selectedImageUrl) {
                            showDialog = false
                        }
                    }

                    // Các phần khác của giao diện
                    if (product.imageMota.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(16.dp))
                        Text("Ảnh mô tả", fontWeight = FontWeight.Bold, fontSize = 16.sp, modifier = Modifier.padding(start = 8.dp))
                        Spacer(modifier = Modifier.height(8.dp))

                        // Lặp qua danh sách hình ảnh mô tả và hiển thị chúng trong LazyRow
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().padding(start = 8.dp)
                        ) {
                            itemsIndexed(product.imageMota) { index, imageUrl ->
                                AsyncImage(
                                    model = imageUrl.replace("http://", "https://"),
                                    contentDescription = "Ảnh mô tả $index",
                                    contentScale = ContentScale.Crop,
                                    modifier = Modifier
                                        .size(120.dp)
                                        .clip(RoundedCornerShape(8.dp))
                                        .clickable {
                                            selectedImageUrl = imageUrl.replace("http://", "https://")
                                            showDialog = true
                                        },
                                    placeholder = painterResource(id = R.drawable.ic_noicom),
                                    error = painterResource(id = R.drawable.ic_xemay)
                                )
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .padding(horizontal = 16.dp)
                            .fillMaxWidth(),
                    ) {
                        Text(
                            modifier = Modifier.align(Alignment.End),
                            text = "Đã đăng lúc ${formattedTime ?: "gần đây"}",
                            fontSize = 12.sp,
                            color = Color.Gray
                        )
                        // Tên, giá, thời gian đăng
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Column() {
                                Text(
                                    text = product.productName,
                                    fontSize = 20.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color.Black
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = formatPrice(product.price),
                                    fontSize = 24.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Red
                                )
                            }
                            Spacer(modifier = Modifier.weight(1f))
                        }

                        Divider(
                            color = Blue_text,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )

                        // Thông tin mặt hàng
                        Column {
                            Text(
                                "Thông tin mặt hàng",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            InfoRow("Loại", product.category)
                            InfoRow("Tình trạng giá", if (product.negotiable) "Có thể thương lượng" else "Không trả giá")
                            InfoRow("Giao hàng", if (product.freeShip) "Miễn phí ship" else "Không có freeship")
                            Row {
                                Icon(
                                    painter = painterResource(id = R.drawable.ic_location),
                                    modifier = Modifier.size(18.dp),
                                    contentDescription = null,
                                    tint = Color.DarkGray
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = product.address,
                                    fontSize = 12.sp,
                                    color = Color.DarkGray
                                )
                            }
                        }

                        Divider(
                            color = Blue_text,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )

                        // Mô tả mặt hàng
                        Column {
                            Text(
                                "Mô tả mặt hàng",
                                fontSize = 16.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = Color.Black
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(text = product.details, fontSize = 14.sp, color = Color.DarkGray)
                        }

                        Divider(
                            color = Blue_text,
                            thickness = 1.dp,
                            modifier = Modifier.padding(vertical = 12.dp)
                        )

                        // Thông tin người đăng + nút liên hệ
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Image(
                                painter = rememberAsyncImagePainter(product.userAvatar),
                                modifier = Modifier
                                    .size(48.dp)
                                    .clip(CircleShape),
                                contentDescription = null
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = product.userName ?: "Không rõ người đăng",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = Color.Black
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(16.dp))

                        Button(
                            onClick = {
                                val intent = Intent(Intent.ACTION_DIAL).apply {
                                    data = "tel:$phoneNumber".toUri()
                                }
                                context.startActivity(intent)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = Blue_text),
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(bottom = 12.dp)
                        ) {
                            Text(
                                text = "Liên hệ ${product.numberUser}",
                                color = Color.White,
                                fontSize = 16.sp
                            )
                        }
                        Spacer(modifier = Modifier.height(16.dp))
                    }
                }
            }
        }
    }
}

// InfoRow để hiển thị thông tin mặt hàng
@Composable
fun InfoRow(label: String, value: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween
    ) {
        Text(text = "$label:", fontWeight = FontWeight.Medium, color = Color.Black, fontSize = 12.sp)
        Text(text = value, color = Color.Black, fontSize = 12.sp)
    }
    Spacer(modifier = Modifier.height(6.dp))
}

@Composable
fun FullScreenImageDialog(imageUrl: String, onDismiss: () -> Unit) {
    var scale by remember { mutableStateOf(1f) }
    var offset by remember { mutableStateOf(Offset.Zero) }

    Dialog(onDismissRequest = onDismiss) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.Black.copy(alpha = 0.85f))
                .pointerInput(Unit) {
                    detectTransformGestures { _, pan, zoom, _ ->
                        scale = (scale * zoom).coerceIn(1f, 5f)
                        offset += pan
                    }
                },
            contentAlignment = Alignment.Center
        ) {
            AsyncImage(
                model = imageUrl.replace("http://", "https://"),
                contentDescription = "Ảnh phóng to",
                contentScale = ContentScale.Fit,
                modifier = Modifier
                    .graphicsLayer(
                        scaleX = scale,
                        scaleY = scale,
                        translationX = offset.x,
                        translationY = offset.y
                    )
                    .clickable { onDismiss() }, // Nhấn để thoát
                placeholder = painterResource(id = R.drawable.ic_noicom),
                error = painterResource(id = R.drawable.ic_xemay)
            )
        }
    }
}
