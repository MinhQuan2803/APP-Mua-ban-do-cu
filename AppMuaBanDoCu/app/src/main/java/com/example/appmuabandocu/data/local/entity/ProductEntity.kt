package com.example.appmuabandocu.data.local.entity

import androidx.room.Entity
import androidx.room.PrimaryKey
import androidx.room.TypeConverters
import com.example.appmuabandocu.data.local.converter.StringListConverter
import com.example.appmuabandocu.model.Product
import java.util.Date

/**
 * Entity đại diện cho một sản phẩm trong cơ sở dữ liệu Room
 */
@Entity(tableName = "products")
@TypeConverters(StringListConverter::class)
data class ProductEntity(
    @PrimaryKey
    var id: String = "",
    val productName: String = "",
    val price: String = "",
    val address: String = "",
    val provinceFB: String = "",
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
    val timestamp: Long = Date().time,
    val inStock: Boolean = true,
    val displayed: Boolean = true,
    // Trường bổ sung cho offline
    val isSynced: Boolean = true,
    val isDeleted: Boolean = false
) {
    /**
     * Chuyển đổi Entity sang Model
     */
    fun toProduct(): Product {
        return Product(
            id = id,
            productName = productName,
            price = price,
            address = address,
            provinceFB = provinceFB,
            numberUser = numberUser,
            category = category,
            details = details,
            negotiable = negotiable,
            freeShip = freeShip,
            imageUrl = imageUrl,
            imageMota = imageMota,
            userId = userId,
            userName = userName,
            userAvatar = userAvatar,
            timestamp = timestamp,
            inStock = inStock,
            displayed = displayed
        )
    }

    companion object {
        /**
         * Chuyển đổi Model sang Entity
         */
        fun fromProduct(product: Product, isSynced: Boolean = false): ProductEntity {
            return ProductEntity(
                id = product.id,
                productName = product.productName,
                price = product.price,
                address = product.address,
                provinceFB = product.provinceFB,
                numberUser = product.numberUser,
                category = product.category,
                details = product.details,
                negotiable = product.negotiable,
                freeShip = product.freeShip,
                imageUrl = product.imageUrl,
                imageMota = product.imageMota,
                userId = product.userId,
                userName = product.userName,
                userAvatar = product.userAvatar,
                timestamp = product.timestamp,
                inStock = product.inStock,
                displayed = product.displayed ?: true,
                isSynced = isSynced
            )
        }
    }
}
