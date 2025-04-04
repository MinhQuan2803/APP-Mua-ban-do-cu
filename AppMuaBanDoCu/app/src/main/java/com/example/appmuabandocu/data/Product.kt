package com.example.appmuabandocu.data

data class Product(
    val productId: String = "",  // ID sản phẩm (optional, để quản lý dễ dàng hơn)
    val productName: String = "",
    val price: String = "",
    val address: String = "",
    val category: String = "",
    val details: String = "",
    val freeShip: Boolean = false,
    val negotiable: Boolean = false,
    val imageUrl: String = "",  // nếu bạn có ảnh
    val userId: String = "",   // ID người dùng đăng sản phẩm
    val userName: String = "", // Tên người dùng (optional)
    val userAvatar: String = "" // Avatar người dùng (optional)
)
