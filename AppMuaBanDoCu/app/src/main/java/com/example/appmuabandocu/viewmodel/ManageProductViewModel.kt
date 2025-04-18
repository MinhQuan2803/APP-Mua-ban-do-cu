package com.example.appmuabandocu.viewmodel

import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.appmuabandocu.data.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlin.jvm.java

class ManageProductViewModel : ViewModel() {

    private val db: DatabaseReference = FirebaseDatabase.getInstance().getReference("products")

    private val _productList = mutableStateListOf<Product>()
    val productList: List<Product> get() = _productList

    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    init {
        loadUserProducts()
    }

    // Tải sản phẩm của người dùng từ Firebase
    fun loadUserProducts() {
        isLoading.value = true
        val userId = FirebaseAuth.getInstance().currentUser?.uid

        if (userId != null) {
            db.orderByChild("userId").equalTo(userId).addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val newProducts = mutableListOf<Product>()
                    snapshot.children.forEach { childSnapshot ->
                        val product = childSnapshot.getValue(Product::class.java)
                        product?.let { newProducts.add(it) }
                    }
                    _productList.clear()
                    _productList.addAll(newProducts)
                    isLoading.value = false
                    Log.d("RealtimeDatabase", "Dữ liệu sản phẩm của người dùng đã được tải thành công!")
                }

                override fun onCancelled(error: DatabaseError) {
                    isLoading.value = false
                    errorMessage.value = "Lỗi khi tải dữ liệu: ${error.message}"
                    Log.e("RealtimeDatabase", "Lỗi tải dữ liệu: ", error.toException())
                }
            })
        } else {
            errorMessage.value = "Người dùng chưa đăng nhập!"
            Log.e("RealtimeDatabase", "Người dùng chưa đăng nhập!")
        }
    }

    // Cập nhật trạng thái hiển thị (ẩn/hiện) của sản phẩm
    fun toggleProductDisplay(product: Product) {
        val productId = product.id
        val updatedProduct = product.displayed?.let { product.copy(displayed = !it) }

        if (productId.isNotEmpty()) {
            db.child(productId).setValue(updatedProduct)
                .addOnSuccessListener {
                    Log.d("RealtimeDatabase", "Cập nhật hiển thị sản phẩm thành công!")
                    _message.value = "Cập nhật hiển thị sản phẩm thành công!"
                }
                .addOnFailureListener { e ->
                    errorMessage.value = "Lỗi cập nhật hiển thị sản phẩm: ${e.message}"
                    Log.e("RealtimeDatabase", "Lỗi cập nhật hiển thị sản phẩm: ", e)
                }
        }
    }

    // Xóa sản phẩm khỏi Firebase
    fun deleteProduct(product: Product) {
        val productId = product.id

        if (productId.isNotEmpty()) {
            db.child(productId).removeValue()
                .addOnSuccessListener {
                    Log.d("RealtimeDatabase", "Xóa sản phẩm thành công!")
                    _message.value = "Xóa sản phẩm thành công!"
                }
                .addOnFailureListener { e ->
                    errorMessage.value = "Lỗi xóa sản phẩm: ${e.message}"
                    Log.e("RealtimeDatabase", "Lỗi xóa sản phẩm: ", e)
                }
        }
    }
}