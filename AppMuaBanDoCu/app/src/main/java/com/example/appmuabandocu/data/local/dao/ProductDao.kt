package com.example.appmuabandocu.data.local.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update
import com.example.appmuabandocu.data.local.entity.ProductEntity
import kotlinx.coroutines.flow.Flow

/**
 * Data Access Object (DAO) cho ProductEntity
 */
@Dao
interface ProductDao {
    /**
     * Lấy tất cả sản phẩm dưới dạng Flow để tự động cập nhật UI khi dữ liệu thay đổi
     */
    @Query("SELECT * FROM products WHERE isDeleted = 0 ORDER BY timestamp DESC")
    fun getAllProducts(): Flow<List<ProductEntity>>

    /**
     * Lấy chi tiết một sản phẩm theo ID
     */
    @Query("SELECT * FROM products WHERE id = :productId AND isDeleted = 0")
    suspend fun getProductById(productId: String): ProductEntity?

    /**
     * Lấy danh sách sản phẩm theo danh mục
     */
    @Query("SELECT * FROM products WHERE category = :category AND isDeleted = 0 ORDER BY timestamp DESC")
    fun getProductsByCategory(category: String): Flow<List<ProductEntity>>

    /**
     * Lấy danh sách sản phẩm của một người bán
     */
    @Query("SELECT * FROM products WHERE userId = :userId AND isDeleted = 0 ORDER BY timestamp DESC")
    fun getProductsByUser(userId: String): Flow<List<ProductEntity>>

    /**
     * Chèn nhiều sản phẩm vào cơ sở dữ liệu, thay thế nếu trùng ID
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProducts(products: List<ProductEntity>)

    /**
     * Chèn một sản phẩm vào cơ sở dữ liệu, thay thế nếu trùng ID
     */
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertProduct(product: ProductEntity): Long

    /**
     * Cập nhật thông tin sản phẩm
     */
    @Update
    suspend fun updateProduct(product: ProductEntity)

    /**
     * Xóa mềm một sản phẩm (đánh dấu là đã xóa)
     */
    @Query("UPDATE products SET isDeleted = 1, timestamp = :timestamp WHERE id = :productId")
    suspend fun softDeleteProduct(productId: String, timestamp: Long = System.currentTimeMillis())

    /**
     * Xóa cứng sản phẩm khỏi cơ sở dữ liệu
     */
    @Query("DELETE FROM products WHERE id = :productId")
    suspend fun hardDeleteProduct(productId: String)

    /**
     * Lấy danh sách sản phẩm chưa đồng bộ với server
     */
    @Query("SELECT * FROM products WHERE isSynced = 0")
    suspend fun getUnsyncedProducts(): List<ProductEntity>

    /**
     * Đánh dấu sản phẩm đã được đồng bộ
     */
    @Query("UPDATE products SET isSynced = 1 WHERE id = :productId")
    suspend fun markProductAsSynced(productId: String)

    /**
     * Tìm kiếm sản phẩm theo tên hoặc mô tả
     */
    @Query("SELECT * FROM products WHERE (productName LIKE '%' || :query || '%' OR details LIKE '%' || :query || '%') AND isDeleted = 0 ORDER BY timestamp DESC")
    fun searchProducts(query: String): Flow<List<ProductEntity>>
}
