package com.example.appmuabandocu.data
data class Product(
    val id: String = "",
    val productName: String = "",
    val price: String = "",
    val address: String = "",
    val category: String = "",
    val details: String = "",
    val negotiable: Boolean = false,
    val freeShip: Boolean = false,
    val imageUrl: String = "", // ảnh bìa
    val imageMota: List<String> = emptyList(), // các ảnh mô tả
    val userId: String = "",
    val userName: String = "",
    val userAvatar: String = "",
    val createdTime: String = "",

)
