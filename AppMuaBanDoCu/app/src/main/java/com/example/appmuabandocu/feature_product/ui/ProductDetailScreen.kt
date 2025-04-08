package com.example.appmuabandocu.feature_product.ui

import ProductViewModel
import android.location.Criteria
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.KeyboardArrowLeft
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.ClipOp
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
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.ui.platform.LocalContext
import com.google.firebase.auth.FirebaseAuth
@Composable
fun ProductDetailScreen(
    auth: FirebaseAuth,
    navController: NavController,
    id: String,
    viewModel: ProductViewModel = viewModel()
) {
    val product = viewModel.productList.find { it.id == id }
    val context = LocalContext.current
    val user = auth.currentUser
    if (product == null) {
        Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
            Text("Không tìm thấy sản phẩm", color = Color.Red)
        }
    } else {
        val screenWidth = LocalConfiguration.current.screenWidthDp.dp

        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(rememberScrollState())
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
                    IconButton(onClick = { }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            modifier = Modifier.size(35.dp),
                            contentDescription = "more"
                        )
                    }
                }
            }

            // Hình ảnh sản phẩm
            AsyncImage(
                model = product.imageUrl.replace("http://", "https://"),
                contentDescription = "Hình sản phẩm",
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .aspectRatio(1.5f),

                placeholder = painterResource(id = R.drawable.ic_noicom),
                error = painterResource(id = R.drawable.ic_xemay)
            )

            Column(modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = product.productName,
                            fontSize = 20.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${product.price} đ",
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color.Red
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Text(
                        text = "Đã đăng ${product.createdTime ?: "gần đây"}",
                        fontSize = 12.sp,
                        color = Color.Gray
                    )
                }

                Divider(
                    color = Blue_text,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                Column {
                    Text("Thông tin mặt hàng", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    InfoRow("Loại", product.category)
                    InfoRow("Tình trạng giá", if (product.negotiable) "Có thể thương lượng" else "Không trả giá")
                    InfoRow("Giao hàng", if (product.freeShip) "Miễn phí ship" else "Không có freeship")
                }

                Divider(
                    color = Blue_text,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                Column {
                    Text("Mô tả mặt hàng", fontSize = 18.sp, fontWeight = FontWeight.SemiBold)
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(text = product.details, fontSize = 16.sp, color = Color.DarkGray)
                }

                Divider(
                    color = Blue_text,
                    thickness = 1.dp,
                    modifier = Modifier.padding(vertical = 12.dp)
                )

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Image(
                        painter = rememberAsyncImagePainter(user?.photoUrl),
                        modifier = Modifier
                            .size(48.dp)
                            .clip(CircleShape),
                        contentDescription = null
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = product.userName ?: "Không rõ người đăng",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.Bold
                        )
                        Text("Đánh giá: 4.3/5", color = Color.Gray)
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    Button(
                        onClick = { },
                        colors = ButtonDefaults.buttonColors(containerColor = Blue_text),
                        modifier = Modifier.padding(4.dp)
                    ) {
                        Text("Theo dõi")
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
        Text(text = "$label:", fontWeight = FontWeight.Medium)
        Text(text = value)
    }
    Spacer(modifier = Modifier.height(6.dp))
}
