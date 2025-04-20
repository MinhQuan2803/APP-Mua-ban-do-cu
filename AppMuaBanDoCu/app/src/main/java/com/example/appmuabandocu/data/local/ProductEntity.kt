package com.example.appmuabandocu.data.local

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "products")
data class ProductEntity(
    @PrimaryKey(autoGenerate = true)
    val localId: Int = 0, // ID tự sinh trong Room

    val id: String = "", // ID từ Realtime nếu có
    val productName: String = "",
    val price: String = "",
    val address: String = "",
    val numberUser: String = "",
    val category: String = "",
    val details: String = "",
    val negotiable: Boolean = false,
    val freeShip: Boolean = false,
    val imageUrl: String = "",
    val imageMota: List<String> = emptyList(),
    val userId: String = "",
    val userName: String = "",
    val userAvatar: String = "",
    val timestamp: Long = System.currentTimeMillis(),
    val inStock: Boolean = true,
    val displayed: Boolean? = true
)
