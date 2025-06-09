package com.example.appmuabandocu.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmuabandocu.model.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import java.util.Date

class ProductViewModel : ViewModel() {

    private val _productList = mutableStateListOf<Product>()
    val productList: List<Product> get() = _productList

    private val db: DatabaseReference = FirebaseDatabase.getInstance().getReference("products")

    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    private val _isRefreshing = MutableStateFlow(false)
    val isRefreshing: StateFlow<Boolean> = _isRefreshing


    init {
        loadProductsRealtime()
    }

    private val _productsByCategory = MutableStateFlow<List<Product>>(emptyList())
    val productsByCategory: StateFlow<List<Product>> = _productsByCategory







    // Tải dữ liệu sản phẩm từ Firebase Realtime Database
    fun loadProductsRealtime() {
        isLoading.value = true

        db.addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                val newProducts = mutableListOf<Product>()
                snapshot.children.forEach { childSnapshot ->
                    val product = childSnapshot.getValue(Product::class.java)
                    product?.let { newProducts.add(it) }
                }
                _productList.clear()
                _productList.addAll(newProducts)
                isLoading.value = false
                Log.d("RealtimeDatabase", "Dữ liệu đã cập nhật theo thời gian thực!")
            }

            override fun onCancelled(error: DatabaseError) {
                isLoading.value = false
                errorMessage.value = "Lỗi khi tải dữ liệu realtime: ${error.message}"
                Log.e("RealtimeDatabase", "Lỗi realtime: ", error.toException())
            }
        })
    }


    // Đăng sản phẩm mới lên Firebase
    fun postProduct(product: Product) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid
        val productWithTimeAndStock = product.copy(
            timestamp = Date().time,
            inStock = true
        )

        if (userId != null) {
            val productRef = db.push() // tạo node mới, tự sinh id
            val productId = productRef.key ?: return

            // Gán userId và id vào product
            val productData = product.copy(
                id = productId,
                userId = userId,
                timestamp = Date().time,
                inStock = true
            )

            productRef.setValue(productData)
                .addOnSuccessListener {
                    Log.d("RealtimeDatabase", "Sản phẩm đã được đăng thành công!")
                    _message.value = "Sản phẩm đã được đăng thành công!"
                    loadProductsRealtime()
                }
                .addOnFailureListener { e ->
                    errorMessage.value = "Lỗi đăng sản phẩm: ${e.message}"
                    Log.e("RealtimeDatabase", "Lỗi đăng sản phẩm: ", e)
                    _message.value = "Lỗi đăng sản phẩm: ${e.message}"
                }
        } else {
            errorMessage.value = "Người dùng chưa đăng nhập!"
            Log.e("RealtimeDatabase", "Người dùng chưa đăng nhập!")
            _message.value = "Người dùng chưa đăng nhập!"
        }
    }


    // Hàm để thay đổi trạng thái ẩn/hiện của một sản phẩm
    fun toggleProductVisibility(productId: String) {
        _productList.find { it.id == productId }?.let {
            val updatedProduct = it.copy(displayed = it.displayed?.not()) // Đảo trạng thái hiển thị
            _productList[_productList.indexOf(it)] = updatedProduct
        }
    }

    // Hàm để lấy danh sách sản phẩm (chỉ trả về sản phẩm không bị ẩn)
    fun getVisibleProducts(): List<Product> {
        return _productList.filter { it.displayed == true }
    }

    // Cập nhật danh sách sản phẩm (ví dụ: gọi từ Firestore)
    fun setProductList(products: List<Product>) {
        _productList.clear()
        _productList.addAll(products)
    }

    fun refreshProducts() {
        viewModelScope.launch {
            _isRefreshing.value = true
            // Giả sử gọi lại API hoặc Firebase
            loadProductsRealtime() // hoặc loadVisibleProducts()
            delay(1000) // delay 1s để hiển thị hiệu ứng rõ ràng (tùy)
            _isRefreshing.value = false
        }
    }
    fun getProductsByCategory(category: String) {
        val dbRef = FirebaseDatabase.getInstance().getReference("products")

        dbRef.orderByChild("category").equalTo(category)
            .addListenerForSingleValueEvent(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val productList = mutableListOf<Product>()
                    for (child in snapshot.children) {
                        val product = child.getValue(Product::class.java)
                        product?.let { productList.add(it) }
                    }
                    _productsByCategory.value = productList
                }

                override fun onCancelled(error: DatabaseError) {
                    // Handle error
                }
            })
    }


}

