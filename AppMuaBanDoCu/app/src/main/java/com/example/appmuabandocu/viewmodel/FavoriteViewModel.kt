package com.example.appmuabandocu.viewmodel

import android.util.Log
import androidx.lifecycle.ViewModel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class FavoriteViewModel : ViewModel() {

    private val _favoriteProductIds = MutableStateFlow<Set<String>>(emptySet())
    val favoriteProductIds: StateFlow<Set<String>> = _favoriteProductIds

    private val db = FirebaseDatabase.getInstance().getReference("users")
    private val userId = FirebaseAuth.getInstance().currentUser?.uid

    init {
        // Kiểm tra người dùng đã đăng nhập chưa
        userId?.let {
            loadFavorites(it)
        } ?: run {
            Log.e("FavoriteViewModel", "User is not logged in.")
        }
    }

    private fun loadFavorites(userId: String) {
        // Lắng nghe sự thay đổi dữ liệu yêu thích trong thời gian thực
        db.child(userId).child("favorites").addValueEventListener(object : ValueEventListener {
            override fun onDataChange(snapshot: DataSnapshot) {
                // Khi có sự thay đổi, cập nhật lại danh sách yêu thích
                val favorites = snapshot.children.mapNotNull { it.key }.toSet()
                _favoriteProductIds.value = favorites
                Log.d("FavoriteViewModel", "Favorites loaded: $favorites")
            }

            override fun onCancelled(error: DatabaseError) {
                Log.e("FavoriteViewModel", "Error loading favorites: ${error.message}")
            }
        })
    }

    fun toggleFavorite(productId: String) {
        userId?.let { uid ->
            val favoriteRef = db.child(uid).child("favorites").child(productId)

            // Kiểm tra xem sản phẩm đã yêu thích hay chưa
            if (_favoriteProductIds.value.contains(productId)) {
                // Nếu đã yêu thích, xóa khỏi yêu thích
                favoriteRef.removeValue().addOnSuccessListener {
                    // Cập nhật trạng thái ngay sau khi xóa
                    _favoriteProductIds.value = _favoriteProductIds.value - productId
                    Log.d("FavoriteViewModel", "Product removed from favorites: $productId")
                }.addOnFailureListener {
                    Log.e("FavoriteViewModel", "Error removing product from favorites: ${it.message}")
                }
            } else {
                // Nếu chưa yêu thích, thêm vào danh sách yêu thích
                favoriteRef.setValue(true).addOnSuccessListener {
                    // Cập nhật trạng thái ngay sau khi thêm
                    _favoriteProductIds.value = _favoriteProductIds.value + productId
                    Log.d("FavoriteViewModel", "Product added to favorites: $productId")
                }.addOnFailureListener {
                    Log.e("FavoriteViewModel", "Error adding product to favorites: ${it.message}")
                }
            }
        }
    }

    fun isFavorite(productId: String): Boolean {
        return _favoriteProductIds.value.contains(productId)
    }
}
