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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.BasicTextField
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Switch
import androidx.compose.material3.SwitchDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.text.font.FontWeight.Companion.Bold
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.rememberAsyncImagePainter
import com.example.appmuabandocu.ui.theme.Blue_text
import com.example.appmuabandocu.R
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.auth.FirebaseAuth
@Composable
fun AddProductScreen(modifier: Modifier = Modifier,category: String) {
    var imageUri by remember { mutableStateOf<Uri?>(null) }
    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetContent()
    ) { uri: Uri? -> imageUri = uri }
    val context = LocalContext.current
    var productName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var negotiable by remember { mutableStateOf(false) }
    var freeShip by remember { mutableStateOf(false) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Spacer(modifier = Modifier.padding(16.dp))
        Text(
            text = "Đăng bán mặt hàng",
            modifier = Modifier.padding(bottom = 16.dp),
            fontWeight = Bold,
            fontSize = 30.sp,
            color = Blue_text
        )
        Divider(
            color = Blue_text,
            thickness = 1.dp,
            modifier = Modifier.padding(bottom = 16.dp)
        )
        Column(
            modifier = Modifier
                .fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            Row(
                modifier = Modifier,
                horizontalArrangement = Arrangement.Start,
                verticalAlignment = Alignment.CenterVertically
            ) {
//            if (imageUri == null) {
//                Box(
//                    modifier = Modifier
//                        .size(100.dp)
//                        .background(Color.LightGray, RoundedCornerShape(8.dp))
//                        .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
//                        .clickable { imagePicker.launch("image/*") },
//                    contentAlignment = Alignment.Center
//                ) {
//                    Text("+", fontSize = 30.sp, color = Color.Black)
//                }
//            }
//            else {
                Image(
                    painter = painterResource(R.drawable.product),

//                    painter = rememberAsyncImagePainter(imageUri),
                    contentDescription = "Selected Image",
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(100.dp)
                        .border(2.dp, Color.Gray, RoundedCornerShape(8.dp))
                )

                Spacer(modifier = Modifier.width(8.dp))

                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Black, RoundedCornerShape(8.dp),)
                        .clickable { imagePicker.launch("image/*") },
                    contentAlignment = Alignment.Center
                ) {
                    Text("+", fontSize = 30.sp, color = Color.Black)
                }
//            }
            }


        }

        Box(

        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth(),
            ){
                OutlinedTextField(
                    value = productName,
                    onValueChange = { productName = it },
                    label = { Text("Tên mặt hàng") },
                    singleLine = true,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )

                OutlinedTextField(
                    value = category,
                    onValueChange = { },
                    label = { Text("Loại mặt hàng") },
                    singleLine = true,
                    readOnly = true,
                    modifier = Modifier.fillMaxWidth().padding(8.dp)
                )

                Row(verticalAlignment = Alignment.CenterVertically) {
                    OutlinedTextField(
                        value = price,
                        onValueChange = { price = it },
                        label = { Text("Giá bán") },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                        modifier = Modifier.padding(8.dp)
                            .width(200.dp)
                    )
                    Spacer(modifier = Modifier.weight(1f))

                    var isChecked by remember { mutableStateOf(false) }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Cho trả giá",)
                        Checkbox(
                            checked = isChecked,
                            onCheckedChange = {isChecked = it},
                            colors = CheckboxDefaults.colors(checkedColor = Color.Blue)
                        )
                    }
                }
                OutlinedTextField(
                    value = address,
                    onValueChange = { address = it },
                    label = { Text("Địa chỉ") },
                    singleLine = true,
                    modifier = Modifier.padding(8.dp)
                        .fillMaxWidth()
                )

                var isCheckedFreeShip by remember { mutableStateOf(false) }
                Row(
                    modifier = Modifier.padding(8.dp)
                        .fillMaxWidth(),
                    horizontalArrangement = Arrangement.Start,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Miễn phí vận chuyển")
                    Checkbox(
                        checked = isCheckedFreeShip,
                        onCheckedChange = {isCheckedFreeShip = it},
                        colors = CheckboxDefaults.colors(checkedColor = Color.Blue)
                    )
                }

                OutlinedTextField(
                    value = details,
                    onValueChange = { details = it },
                    label = { Text("Thông tin chi tiết") },
                    singleLine = false,
                    maxLines = 5,
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp)
                        .height(150.dp)
                )

            }
        }
        Spacer(modifier = Modifier.height(16.dp))
        Button(
            onClick = {
                saveProductToFirestore(context, productName, category, price, address, details, negotiable, freeShip)
            },
            colors = ButtonDefaults.buttonColors(containerColor = Blue_text),
            modifier = Modifier.size(width = 150.dp, height = 50.dp)
        ) {
            Text("Đăng",
                fontSize = 20.sp, color = Color.White)
        }
    }
}

fun saveProductToFirestore(context: Context,productName: String, category: String, price: String, address: String, details: String, negotiable: Boolean, freeShip: Boolean) {
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
@Composable
fun CustomOutlinedTextField(value: String, onValueChange: (String) -> Unit, label: String, singleLine: Boolean = true) {
    OutlinedTextField(
        value = value,
        onValueChange = onValueChange,
        label = { Text(label) },
        textStyle = TextStyle(fontSize = 16.sp),
        singleLine = singleLine,
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}


@Composable
fun SwitchWithLabel(label: String, state: Boolean, onToggle: (Boolean) -> Unit) {
    Row(verticalAlignment = Alignment.CenterVertically) {
        Text(label, modifier = Modifier.weight(1f))
        Switch(checked = state, onCheckedChange = onToggle, colors = SwitchDefaults.colors(checkedThumbColor = Color.Blue))
    }
}


