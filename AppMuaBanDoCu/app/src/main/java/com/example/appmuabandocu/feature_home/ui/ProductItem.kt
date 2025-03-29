package com.example.appmuabandocu.feature_home.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.appmuabandocu.ui.theme.Blue_text

@Preview(showBackground = true)
@Composable
fun myscreen(){
    ProductItem(
        userName = "HuyNguyen",
        location = "Thảo Điền - TP.HCM",
        productName = "Camera mẫu mới giá rẻ",
        price = "2.3tr",
        imageUrl = "https://picsum.photos/seed/picsum/200/300",
        time = "1 h"
    )
}


@Composable
fun ProductItem(
    userName: String,
    location: String,
    productName: String,
    price: String,
    imageUrl: String, // link ảnh từ internet
    modifier: Modifier = Modifier,
    time: String
) {
    Card(
        modifier = modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clip(RoundedCornerShape(12.dp))
            .shadow(2.dp, RoundedCornerShape(12.dp))
            .clickable { /* TODO: Handle click */ },
        colors = CardDefaults.cardColors(containerColor = Color.White)
    ) {
        Divider(
            color = Blue_text,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 8.dp)
        )
        Column(modifier = Modifier.padding(12.dp)) {

            // Header: Avatar + User + Location
            Row(verticalAlignment = Alignment.CenterVertically) {
                // Avatar tạm dùng icon
                Icon(
                    imageVector = Icons.Default.AccountCircle,
                    contentDescription = null,
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Column {
                    Text(text = userName, fontWeight = FontWeight.Bold)
                    Row {
                        Text(text = time, fontSize = 12.sp, color = Color.Gray)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(text = location, fontSize = 12.sp, color = Color.Gray)

                    }
                }
                Spacer(modifier = Modifier.weight(1f)) // Spacer đẩy dấu ba chấm sang phải

                Icon(
                    imageVector = Icons.Default.MoreVert, // dấu ba chấm dọc
                    contentDescription = "More"
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Product Image
            AsyncImage(
                model = imageUrl,
                contentDescription = null,
                contentScale = ContentScale.Crop,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(8.dp))
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Product Info
            Text(text = "Tên sản phẩm: $productName", fontWeight = FontWeight.Bold, fontSize = 16.sp)
            Text(text = "Giá bán: $price", color = Color.Black, fontSize = 15.sp)

            Spacer(modifier = Modifier.height(8.dp))

            // Contact Button
//            Button(
//                onClick = { /* TODO: Gọi số hoặc mở chat */ },
//                modifier = Modifier
//                    .fillMaxWidth()
//                    .height(45.dp),
//                colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2196F3))
//            ) {
//                Icon(Icons.Default.Phone, contentDescription = null)
//                Spacer(modifier = Modifier.width(8.dp))
//                Text(text = "Liên hệ ngay", color = Color.White)
//            }

            TextButton(
                onClick = { /* TODO: Gọi số hoặc mở chat */ },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(45.dp),
            ) {
                Icon(
                    imageVector = Icons.Default.Phone,
                    contentDescription = null,
                    tint = Blue_text
                )
                Text(text = "Liên hệ ngay", color = Blue_text)
            }
        }
    }
}
