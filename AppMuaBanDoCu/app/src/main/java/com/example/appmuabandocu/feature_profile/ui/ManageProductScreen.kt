package com.example.appmuabandocu.feature_profile.ui


import androidx.compose.foundation.Image
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.Icon
import androidx.compose.material.MaterialTheme
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.appmuabandocu.data.Product
import com.example.appmuabandocu.viewmodel.ManageProductViewModel
import com.example.appmuabandocu.R
import com.example.appmuabandocu.ui.theme.Blue_text




@Composable
fun ManageProductScreen(viewModel: ManageProductViewModel, navController: NavController) {
    val isLoading = viewModel.isLoading.value
    val errorMessage = viewModel.errorMessage.value
    val message = viewModel.message.collectAsState().value
    val productList = viewModel.productList
    val productToDelete = viewModel.productToDelete.value


    Column(
        modifier = Modifier.fillMaxSize()
            .padding(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally
    ){
        Spacer(modifier = Modifier.padding(16.dp))
        Row (
            modifier = Modifier
                .fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.Start
        ){
            Icon(
                painter = painterResource(id = R.drawable.back_left),
                contentDescription = "Back",
                modifier = Modifier.padding(end = 8.dp)
                    .clickable { navController.popBackStack() },
                tint = Blue_text
            )
            androidx.compose.material.Text(
                "Bài viết của bạn",
                fontSize = 22.sp,
                fontWeight = FontWeight.Bold,
                color = Blue_text,
                modifier = Modifier.fillMaxWidth().padding(top = 16.dp, bottom = 16.dp),
                textAlign = TextAlign.Center
            )
        }
        Spacer(modifier = Modifier.padding(16.dp))


        if (isLoading) {
            Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                CircularProgressIndicator()
            }
        } else {
            errorMessage?.let {
                Text(text = it, color = Color.Red, modifier = Modifier.padding(8.dp))
            }
            if (message.isNotEmpty()) {
                Text(text = message, color = Color(0xFF2E7D32), modifier = Modifier.padding(8.dp))
            }
            if (productList.isEmpty()) {
                Text("Bạn chưa đăng sản phẩm nào.", modifier = Modifier.padding(top = 16.dp))
            } else {
                LazyColumn(modifier = Modifier.fillMaxSize()) {
                    items(productList.size) { product ->
                        ProductItem(product = productList[product], viewModel = viewModel,navController)

                    }
                }
            }
        }


        // Xác nhận xóa sản phẩm
        productToDelete?.let { product ->
            androidx.compose.material.AlertDialog(
                onDismissRequest = { viewModel.cancelDelete() },
                title = { Text("Xác nhận xóa", color = Color.Black) },
                text = { Text("Bạn có chắc muốn xóa sản phẩm '${product.productName}' không?", color = Color.Black) },
                confirmButton = {
                    androidx.compose.material.TextButton(onClick = {
                        viewModel.performDelete()
                    }) {
                        Text("Xóa", color = Color.Red)
                    }
                },
                dismissButton = {
                    androidx.compose.material.TextButton(onClick = {
                        viewModel.cancelDelete()
                    }) {
                        Text("Hủy", color = Color.Black)
                    }
                }
            )
        }
    }
}




@Composable
fun ProductItem(product: Product, viewModel: ManageProductViewModel,navController: NavController) {
    val displayed = product.displayed ?: true // Mặc định hiển thị sản phẩm

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
            .clickable {
                navController.navigate("product_detail/${product.id}")
            },
        shape = RoundedCornerShape(8.dp),

    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Hình ảnh sản phẩm
            AsyncImage(
                model = product.imageUrl.replace("http://", "https://"),
                contentDescription = null,
                modifier = Modifier
                    .size(60.dp)
                    .padding(end = 16.dp),
                placeholder = painterResource(id = R.drawable.ic_noicom), // Thêm ảnh placeholder
                error = painterResource(id = R.drawable.ic_condit), // Thêm ảnh khi có lỗi
            )


            // Chi tiết sản phẩm
            Column(
                modifier = Modifier.weight(1f)
            ) {
                Text(text = product.productName, style = MaterialTheme.typography.h6)
                Text(text = "${product.price} VND", style = MaterialTheme.typography.body2)
                Text(text = "Địa chỉ: ${product.address}", style = MaterialTheme.typography.body2)
            }


            // Nút ẩn/hiện sản phẩm
            IconButton(onClick = { viewModel.toggleProductDisplay(product) }) {
                Icon(
                    painter = painterResource(id = if (displayed) R.drawable.ic_visibility else R.drawable.ic_visibility_off),
                    contentDescription = if (displayed) "Ẩn sản phẩm" else "Hiển thị sản phẩm"
                )
            }


            // Nút xóa sản phẩm
            IconButton(onClick = { viewModel.confirmDelete(product) }) {
                Icon(
                    painter = painterResource(id = R.drawable.ic_delete),
                    contentDescription = "Xóa sản phẩm"
                )
            }
        }
    }
}


@Preview(showBackground = true)
@Composable
fun PreviewManageProductScreen() {
    val viewModel = viewModel<ManageProductViewModel>()
    ManageProductScreen(viewModel, NavController(LocalContext.current))
}
