package com.example.appmuabandocu.feature_product.ui

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
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.appmuabandocu.ui.theme.Blue_text
import com.example.appmuabandocu.R

@Composable
fun ProductDetailScreen(modifier: Modifier = Modifier, navController: NavController, id: String) {
    Box (
        modifier = Modifier
            .fillMaxSize()
    ){
        Column() {
            Box (
                modifier = Modifier.fillMaxWidth()
                    .height(92.dp)
                    .background(Blue_text),
                contentAlignment = Alignment.Center
            ){
                // thoát + dấu ba chấm
                Row (
                    modifier = Modifier.fillMaxWidth()
                        .align(Alignment.BottomCenter),
                ){
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Default.KeyboardArrowLeft,
                            contentDescription = "Back",
                            modifier = Modifier.size(40.dp),
                            tint = Color.Black
                        )
                    }
                    Spacer(modifier = Modifier.weight(1f))
                    IconButton(onClick = {  }) {
                        Icon(
                            imageVector = Icons.Default.MoreVert,
                            modifier = Modifier.size(35.dp),
                            contentDescription = "more"
                        )

                    }
                }
            }
            // done thoát + ba chấm

            // Thông tin sản phẩm
            Column(){
                // 1. hình ảnh sản phẩm
                Image(
                    painter = painterResource(id = R.drawable.pd_camera),
//                    painter = rememberAsyncImagePainter("https://via.placeholder.com/400"),
                    contentDescription = "Camera Image",
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(240.dp),
                    contentScale = ContentScale.Crop
                )
                //done hình ảnh sản phẩm

                // 2. thông tin giá + tên sản phẩm + ngày đăng
                Column(modifier = Modifier.padding(16.dp)) {
                    Row {
                        Column {
                            Text(
                                text = "500.000 đ",
                                fontSize = 24.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color.Red
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = "Camera mẫu mới giá rẻ",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                        Spacer(modifier = Modifier.weight(1f))
                        Text(text = "Đã đăng 1 giờ trước", fontSize = 12.sp, color = Color.Gray)

                    }

                    Divider(color = Blue_text, thickness = 1.dp, modifier = Modifier.padding(vertical = 12.dp))

                    // 3. thông tin mặt hàng
                    Box(modifier = Modifier.fillMaxWidth()){
                        Column {
                            Text(
                                text = "Thông tin mặt hàng",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            InfoRow(label = "Loại", value = "Camera")
                            InfoRow(label = "Tình trạng", value = "Như mới")
                            InfoRow(label = "Giao hàng", value = "Có thể giao / Tự tới lấy")
                        }

                    }
                    
                    Divider(color = Blue_text, thickness = 1.dp, modifier = Modifier.padding(vertical = 12.dp))

                    // 4. mô tả mặt hàng
                    Box(modifier = Modifier.fillMaxWidth()){
                        Column {
                            Text(
                                text = "Mô tả mặt hàng",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.SemiBold
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "mặt hàng này còn mới nguyên siu chưa bóc tem",
                                fontSize = 16.sp,
                                color = Color.DarkGray
                            )
                        }
                    }

                    Divider(color = Blue_text, thickness = 1.dp, modifier = Modifier.padding(vertical = 12.dp))

                    // 5. thông tin liên hệ
                    Box(modifier = Modifier.fillMaxWidth()){
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box {
                                Row {
                                    Image(
                                        painter = painterResource(id = R.drawable.huynguyen),
                                        modifier = Modifier.size(40.dp)
                                            .clip(CircleShape),
                                        contentDescription = null,
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = "Nguyen Huy",
                                            fontSize = 18.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                        Spacer(modifier = Modifier.height(4.dp))
                                        Text(
                                            text = "Đánh giá: 4.3/5",
                                            color = Color.Gray
                                        )
                                    }
                                }
                            }
                            Spacer(modifier = Modifier.weight(1f))
                            Button(
                                onClick = { /*TODO*/ },
                                colors = ButtonDefaults.buttonColors(
                                    containerColor = Blue_text
                                ),
                                modifier = Modifier
                                    .padding(2.dp)
                            ){
                                Text(text = "Theo dõi")
                            }
                        }
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
