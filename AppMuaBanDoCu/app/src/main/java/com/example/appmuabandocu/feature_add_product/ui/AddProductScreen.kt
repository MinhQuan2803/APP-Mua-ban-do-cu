package com.example.appmuabandocu.feature_add_product.ui

import android.content.Context
import android.net.Uri
import android.os.Environment
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.material.icons.filled.Error
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.content.FileProvider
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.rememberAsyncImagePainter
import com.example.appmuabandocu.data.Product
import com.example.appmuabandocu.data.uploadImageToCloudinary
import com.example.appmuabandocu.feature_add_product.CurrencyInputTransformation
import com.example.appmuabandocu.ui.theme.Blue_text
import com.example.appmuabandocu.viewmodel.AddressViewModel
import com.example.appmuabandocu.viewmodel.ProductViewModel
import com.google.firebase.auth.FirebaseAuth
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Throws(IOException::class)
fun createImageFile(context: Context): File {
    // Tạo tên file ảnh
    val timeStamp: String = SimpleDateFormat("yyyyMMdd_HHmmss", Locale.US).format(Date())
    val storageDir: File? = context.getExternalFilesDir(Environment.DIRECTORY_PICTURES)

    // Tạo file ảnh tạm thời
    val image = File.createTempFile(
        "JPEG_${timeStamp}_",  /* Tiền tố của tên file */
        ".jpg",                /* Đuôi file */
        storageDir             /* Thư mục lưu file */
    )

    // Trả về đường dẫn file ảnh
    return image
}
@Composable
fun AddProductScreen(
    modifier: Modifier = Modifier,
    category: String,
    addressViewModel: AddressViewModel = viewModel(),
    viewModel: ProductViewModel = viewModel(),
    navController: NavController
) {

    // Quan sát trạng thái thông báo từ ViewModel
    val message = viewModel.message.collectAsState().value
    val context = LocalContext.current
    var productName by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var numberUser by remember { mutableStateOf("") }
    var address by remember { mutableStateOf("") }
    var provinceFB by remember { mutableStateOf("") }
    var details by remember { mutableStateOf("") }
    var negotiable by remember { mutableStateOf(false) }
    var freeShip by remember { mutableStateOf(false) }
    var imageUris by remember { mutableStateOf<List<Uri>>(emptyList()) }


    // Biến để lưu danh sách tỉnh, quận, phường
    val provinces by addressViewModel.provinces.collectAsState()
    val districts by addressViewModel.districts.collectAsState()
    val wards by addressViewModel.wards.collectAsState()

    // Biến để lưu tỉnh, quận, phường đã chọn
    val selectedProvince by addressViewModel.selectedProvince.collectAsState()
    val selectedDistrict by addressViewModel.selectedDistrict.collectAsState()
    val selectedWard by addressViewModel.selectedWard.collectAsState()

    // Biến để quản lý trạng thái dropdown
    var expandedProvince by remember { mutableStateOf(false) }
    var expandedDistrict by remember { mutableStateOf(false) }
    var expandedWard by remember { mutableStateOf(false) }

    provinceFB = selectedProvince?.name ?: ""

    address = buildString {
        if (selectedWard != null) append("${selectedWard!!.name}, ")
        if (selectedDistrict != null) append("${selectedDistrict!!.name}, ")
        if (selectedProvince != null) append(selectedProvince!!.name)
    }


    val imagePicker = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.GetMultipleContents()
    ) { uris -> imageUris = imageUris + uris }


    // Lấy thông tin người dùng từ FirebaseAuth
    val userId = FirebaseAuth.getInstance().currentUser?.uid ?: ""
    val userName = FirebaseAuth.getInstance().currentUser?.displayName ?: ""
    val userAvatar = FirebaseAuth.getInstance().currentUser?.photoUrl?.toString() ?: ""

    val cameraImageUri = remember { mutableStateOf<Uri?>(null) }
    fun onImagePicked(uri: Uri) {
        // Thêm ảnh vào danh sách ảnh đã chọn
        imageUris = imageUris + uri
    }
    // Sử dụng rememberLauncherForActivityResult để mở camera
    val cameraLauncher = rememberLauncherForActivityResult(
        contract = ActivityResultContracts.TakePicture()
    ) { success ->
        if (success) {
            cameraImageUri.value?.let { uri ->
                // Xử lý ảnh sau khi chụp
                onImagePicked(uri) // Giả sử bạn có hàm xử lý ảnh đã chọn
            }
        }
    }

//
//    // SnackbarHostState để hiển thị Snackbar
//    val snackbarHostState = remember { SnackbarHostState() }
//
//
//    // Khi có message mới, hiển thị Snackbar
//    LaunchedEffect(message) {
//        if (message.isNotEmpty()) {
//            snackbarHostState.showSnackbar(message)
//        }
//    }


    // Reset UI khi nhận thông báo thành công
    LaunchedEffect(message) {
        if (message == "Sản phẩm đã được đăng thành công!") {
            productName = ""
            price = ""
            address = ""
            provinceFB = ""
            numberUser = ""
            details = ""
            negotiable = false
            freeShip = false
            imageUris = emptyList()
        }
    }


    Column(
        modifier = Modifier
            .fillMaxWidth()
            .padding(16.dp)
            .verticalScroll(rememberScrollState()),
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        Text(
            text = "Đăng bán mặt hàng",
            fontSize = 24.sp,
            color = Blue_text,
            modifier = Modifier.padding(top = 10.dp)
        )


        Divider(
            color = Blue_text,
            thickness = 1.dp,
            modifier = Modifier.padding(vertical = 5.dp)
        )


        // Hiển thị ảnh sản phẩm đã chọn
        LazyRow(
            modifier = Modifier.padding(vertical = 12.dp)
        ) {
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

            item {
                Spacer(modifier = Modifier.width(8.dp))
            }

            item {
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(Color.LightGray, RoundedCornerShape(8.dp))
                        .border(1.dp, Color.Black, RoundedCornerShape(8.dp))
                        .padding(end = 4.dp)
                        .clickable {
                            // Tạo Uri ảnh tạm thời và mở camera
                            val uri = FileProvider.getUriForFile(
                                context,
                                "${context.packageName}.provider",
                                createImageFile(context)
                            )
                            cameraImageUri.value = uri
                            cameraLauncher.launch(uri)
                        },
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        Icons.Default.PhotoCamera,
                        contentDescription = "Camera",
                        tint = Color.Black
                    )
                }
            }
        }


        // Các trường nhập liệu của sản phẩm
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
                onValueChange = { input ->
                    if (input.all { it.isDigit() }) {
                        price = input
                    }
                },
                label = { Text("Giá bán") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                visualTransformation = CurrencyInputTransformation(),
                modifier = Modifier
                    .width(220.dp)
                    .padding(vertical = 8.dp)
            )
            Spacer(modifier = Modifier.weight(1f))
            Checkbox(
                checked = negotiable,
                onCheckedChange = { negotiable = it },
                colors = CheckboxDefaults.colors(checkedColor = Color.Blue)
            )
            Text("Cho trả giá")
        }


        val phoneError = numberUser.isNotEmpty() && !numberUser.matches(Regex("^(0|84)[0-9]{9}$"))
        OutlinedTextField(
            value = numberUser,
            onValueChange = {
                // Chỉ cho nhập số và giới hạn độ dài
                if (it.length <= 11 && it.all { char -> char.isDigit() }) {
                    numberUser = it
                }
            },
            isError = phoneError,
            keyboardOptions = KeyboardOptions(
                keyboardType = KeyboardType.Phone,
                imeAction = ImeAction.Done
            ),
            label = { Text("Số điện thoại") },
            singleLine = true,
            modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
            supportingText = {
                AnimatedVisibility(visible = phoneError) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Error,
                            contentDescription = null,
                            tint = Color.Red,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("Số điện thoại không hợp lệ", color = Color(0xFFFFA726))
                    }
                }
            }
        )

        Spacer(modifier = Modifier.height(16.dp))

        // Tỉnh/Thành phố Dropdown
        OutlinedTextField(
            value = selectedProvince?.name ?: "Chọn tỉnh",
            onValueChange = {},
            label = { Text("Tỉnh/Thành phố") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expandedProvince = !expandedProvince }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            colors = TextFieldDefaults.colors(
                focusedTextColor = Color.Black,
                focusedIndicatorColor = Blue_text,
                unfocusedIndicatorColor = Blue_text,
                unfocusedContainerColor = Color.White,
                unfocusedTextColor = Color.Black
            ),
            singleLine = true
        )
        DropdownMenu(
            expanded = expandedProvince,
            onDismissRequest = { expandedProvince = false }
        ) {
            provinces.forEach { province ->
                DropdownMenuItem(
                    onClick = {
                        addressViewModel.selectProvince(province)
                        expandedProvince = false
                    },
                    text = { Text(province.name) }
                )
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Quận/Huyện Dropdown
        if (selectedProvince != null) {
            OutlinedTextField(
                value = selectedDistrict?.name ?: "Chọn quận/huyện",
                onValueChange = {},
                label = { Text("Quận/Huyện") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { expandedDistrict = !expandedDistrict }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedIndicatorColor = Blue_text,
                    unfocusedIndicatorColor = Blue_text,
                    unfocusedContainerColor = Color.White,
                    unfocusedTextColor = Color.Black
                ),
                singleLine = true
            )
            DropdownMenu(
                expanded = expandedDistrict,
                onDismissRequest = { expandedDistrict = false }
            ) {
                districts.forEach { district ->
                    DropdownMenuItem(
                        onClick = {
                            addressViewModel.selectDistrict(district)
                            expandedDistrict = false
                        },
                        text = { Text(district.name) }
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Xã/Phường Dropdown
        if (selectedDistrict != null) {
            OutlinedTextField(
                value = selectedWard?.name ?: "Chọn xã/phường",
                onValueChange = {},
                label = { Text("Xã/Phường") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { expandedWard = !expandedWard }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedIndicatorColor = Blue_text,
                    unfocusedIndicatorColor = Blue_text,
                    unfocusedContainerColor = Color.White,
                    unfocusedTextColor = Color.Black
                ),
                singleLine = true
            )
            DropdownMenu(
                expanded = expandedWard,
                onDismissRequest = { expandedWard = false }
            ) {
                wards.forEach { ward ->
                    DropdownMenuItem(
                        onClick = {
                            addressViewModel.selectWard(ward)
                            expandedWard = false
                        },
                        text = { Text(ward.name) }
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Thêm trường nhập địa chỉ đường/phố
            OutlinedTextField(
                value = addressViewModel.streetAddress.collectAsState().value,
                onValueChange = { addressViewModel.setStreetAddress(it) },
                label = { Text("Số nhà, tên đường...") },
                modifier = Modifier.fillMaxWidth().padding(vertical = 8.dp),
                singleLine = true,
                colors = TextFieldDefaults.colors(
                    focusedTextColor = Color.Black,
                    focusedIndicatorColor = Blue_text,
                    unfocusedIndicatorColor = Blue_text,
                    unfocusedContainerColor = Color.White,
                    unfocusedTextColor = Color.Black
                )
            )
        }

        Spacer(modifier = Modifier.height(16.dp))


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


        // Button Đăng sản phẩm
        val coroutineScope = rememberCoroutineScope()
        var isPosting by remember { mutableStateOf(false) }
        Button(
            onClick = {
                if (isPosting) return@Button
                // Kiểm tra các trường bắt buộc
                if (productName.isEmpty() || price.isEmpty() || address.isEmpty() || numberUser.isEmpty() || details.isEmpty()) {
                    Toast.makeText(context, "Vui lòng điền đầy đủ thông tin", Toast.LENGTH_SHORT).show()
                    return@Button
                }


                if (imageUris.isEmpty()) {
                    Toast.makeText(context, "Vui lòng chọn ít nhất 1 ảnh", Toast.LENGTH_SHORT)
                        .show()
                    return@Button
                }


                isPosting = true


                // Coroutine để upload ảnh và gửi sản phẩm
                coroutineScope.launch {
                    val urls = imageUris.mapNotNull { uri ->
                        uploadImageToCloudinary(context, uri)
                    }

                    if (urls.isNotEmpty()) {
                        val product = Product(
                            productName = productName,
                            price = price,
                            address = address,
                            provinceFB = provinceFB,
                            numberUser = numberUser,
                            category = category,
                            details = details,
                            negotiable = negotiable,
                            freeShip = freeShip,
                            imageUrl = urls[0], // ảnh bìa
                            imageMota = urls.drop(1), // ảnh mô tả
                            userId = userId,
                            userName = userName,
                            userAvatar = userAvatar
                        )
                        viewModel.postProduct(product)
                        Toast.makeText(context, "Đăng bài viết thành công", Toast.LENGTH_SHORT).show()

                        delay(2000)

                        navController.navigate("homeNav") {
                            popUpTo("homeNav") { inclusive = true }
                        }
                    } else {
                        Toast.makeText(context, "Không thể đăng ảnh nào", Toast.LENGTH_SHORT).show()
                    }
                    isPosting = false
                }
            },
            enabled = !isPosting,
            colors = ButtonDefaults.buttonColors(
                containerColor = if (isPosting) Color.Gray else Blue_text
            ),
            modifier = Modifier.size(width = 150.dp, height = 50.dp)
        ) {
            if (isPosting) {
                CircularProgressIndicator(
                    modifier = Modifier.size(20.dp),
                    color = Color.White,
                    strokeWidth = 2.dp
                )
            } else {
                Text("Đăng", fontSize = 20.sp, color = Color.White)
            }
        }
        Spacer(modifier = Modifier.height(60.dp))
    }
}