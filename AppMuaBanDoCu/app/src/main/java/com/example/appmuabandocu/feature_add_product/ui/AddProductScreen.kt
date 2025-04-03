package com.example.appmuabandocu.feature_add_product.ui

import android.content.Context
import android.net.Uri
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.appmuabandocu.ui.theme.Blue_text
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

@Composable
fun AddProductScreen(modifier: Modifier = Modifier, category: String) {
    val context = LocalContext.current
    var productName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var negotiable by remember { mutableStateOf(false) }
    var freeShip by remember { mutableStateOf(false) }
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }

    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris -> imageUris = imageUris + uris }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(28.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text("Đăng bán mặt hàng", fontSize = 24.sp, color = Blue_text)
        Divider(color = Blue_text, thickness = 1.dp, modifier = Modifier.padding(vertical = 5.dp))

        LazyRow {
            items(imageUris) { uri ->
                Image(
                    painter = rememberAsyncImagePainter(uri),
                    contentDescription = "Selected Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                )
                Spacer(modifier = Modifier.width(8.dp))
            }
            item {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                        .clickable { imagePicker.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", fontSize = 30.sp, color = Color.Black)
                }
            }
        }

        OutlinedTextField(
            value = productName,
            onValueChange = { productName = it },
            label = { Text("Tên mặt hàng") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        OutlinedTextField(
            value = category,
            onValueChange = { },
            label = { Text("Loại mặt hàng") },
            singleLine = true,
            readOnly = true,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            OutlinedTextField(
                value = price,
                onValueChange = { price = it },
                label = { Text("Giá bán") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                modifier = Modifier.width(200.dp).padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(
                checked = negotiable,
                onCheckedChange = { negotiable = it },
                colors = CheckboxDefaults.colors(checkedColor = Color.Blue)
            )
            Text("Cho trả giá")
        }

        OutlinedTextField(
            value = address,
            onValueChange = { address = it },
            label = { Text("Địa chỉ") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp)
        )

        Row(verticalAlignment = Alignment.CenterVertically) {
            Checkbox(
                checked = freeShip,
                onCheckedChange = { freeShip = it },
                colors = CheckboxDefaults.colors(checkedColor = Color.Blue)
            )
            Text("Miễn phí vận chuyển")
        }

        OutlinedTextField(
            value = details,
            onValueChange = { details = it },
            label = { Text("Thông tin chi tiết") },
            singleLine = false,
            maxLines = 5,
            modifier = Modifier.fillMaxWidth().height(150.dp).padding(vertical = 8.dp)
        )

        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                saveProductToFirestore(context, productName, category, price, address, details, negotiable, freeShip)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Blue_text),
            modifier = Modifier.size(width = 150.dp, height = 50.dp)
        ) {
            Text("Đăng", fontSize = 20.sp, color = Color.White)
        }
    }
}

fun saveProductToFirestore(
    context: Context,
    productName: String,
    category: String,
    price: String,
    address: String,
    details: String,
    negotiable: Boolean,
    freeShip: Boolean
) {
    val db = FirebaseFirestore.getInstance()
    val userId = FirebaseAuth.getInstance().currentUser?.uid

    if (userId != null) {
        val productData = hashMapOf(
            "productName" to productName,
            "category" to category,
            "price" to price,
            "address" to address,
            "details" to details,
            "negotiable" to negotiable,
            "freeShip" to freeShip
        )

        db.collection("users").document(userId).collection("products")
            .add(productData)
            .addOnSuccessListener {
                Toast.makeText(context, "Đăng sản phẩm thành công!", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Toast.makeText(context, "Lỗi: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    } else {
        Toast.makeText(context, "Người dùng chưa đăng nhập!", Toast.LENGTH_SHORT).show()
    }
}
