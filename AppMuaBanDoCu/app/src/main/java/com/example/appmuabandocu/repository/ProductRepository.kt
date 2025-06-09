package com.example.appmuabandocu.repository

import com.example.appmuabandocu.data.local.dao.ProductDao
import com.example.appmuabandocu.data.local.entity.ProductEntity
import com.example.appmuabandocu.model.Product
import com.example.appmuabandocu.utils.NetworkConnectivityObserver
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.ktx.toObject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.flow.first
import java.util.Date

/**
 * Repository pattern để xử lý dữ liệu sản phẩm từ cả nguồn cục bộ (Room) và remote (Firebase)
 */
class ProductRepository(
    private val productDao: ProductDao,
    private val firestore: FirebaseFirestore,
    private val networkObserver: NetworkConnectivityObserver
) {
    private val productsCollection = firestore.collection("products")

    /**
     * Lấy tất cả sản phẩm, từ cơ sở dữ liệu cục bộ (ưu tiên) hoặc từ Firestore
     * @return Flow của danh sách sản phẩm
     */
    fun getAllProducts(): Flow<List<Product>> {
        // Không gọi trực tiếp hàm suspend từ đây nữa
        // Thay vào đó, chúng ta sẽ chỉ trả về dữ liệu từ Room
        return productDao.getAllProducts().map { entities ->
            entities.map { it.toProduct() }
        }
    }

    /**
     * Lấy chi tiết một sản phẩm theo ID
     * @param productId ID của sản phẩm
     * @return Sản phẩm nếu tìm thấy, null nếu không tìm thấy
     */
    suspend fun getProductById(productId: String): Product? {
        // Đầu tiên, thử lấy từ cơ sở dữ liệu cục bộ
        val localProduct = productDao.getProductById(productId)

        // Nếu có kết nối mạng và không tìm thấy sản phẩm cục bộ, thử lấy từ Firebase
        if (localProduct == null && networkObserver.isNetworkAvailable.first()) {
            try {
                val remoteProduct = productsCollection.document(productId).get().await()
                    .toObject<Product>() // Lấy trực tiếp đối tượng Product từ Firebase

                if (remoteProduct != null) {
                    // Lưu vào cơ sở dữ liệu cục bộ để sử dụng offline sau này
                    val productEntity = ProductEntity.fromProduct(remoteProduct, true)
                    productDao.insertProduct(productEntity)
                    return remoteProduct
                }
            } catch (e: Exception) {
                // Log exception - có thể fail vì nhiều lý do (timeout, permission, etc)
                e.printStackTrace()
            }
        }

        return localProduct?.toProduct()
    }

    /**
     * Cập nhật sản phẩm lên cả cơ sở dữ liệu cục bộ và Firebase (nếu có kết nối)
     */
    suspend fun updateProduct(product: Product) {
        // Luôn cập nhật vào cơ sở dữ liệu cục bộ
        val productEntity = ProductEntity.fromProduct(
            product.copy(timestamp = Date().time),
            isSynced = networkObserver.isNetworkAvailable.first()
        )

        productDao.updateProduct(productEntity)

        // Nếu có kết nối, cập nhật lên Firebase
        if (networkObserver.isNetworkAvailable.first()) {
            try {
                productsCollection.document(product.id).set(product).await()
                productDao.markProductAsSynced(product.id)
            } catch (e: Exception) {
                e.printStackTrace()
                // Không đánh dấu sản phẩm là đã đồng bộ, sẽ được đồng bộ lại sau
            }
        }
    }

    /**
     * Tạo sản phẩm mới và lưu vào cả cơ sở dữ liệu cục bộ và Firebase (nếu có kết nối)
     */
    suspend fun createProduct(product: Product): String {
        val timestamp = Date().time
        val productId = if (product.id.isBlank()) productsCollection.document().id else product.id

        val newProduct = product.copy(
            id = productId,
            timestamp = timestamp
        )

        // Luôn lưu vào cơ sở dữ liệu cục bộ trước
        val productEntity = ProductEntity.fromProduct(
            newProduct,
            isSynced = networkObserver.isNetworkAvailable.first()
        )

        productDao.insertProduct(productEntity)

        // Nếu có kết nối, lưu lên Firebase
        if (networkObserver.isNetworkAvailable.first()) {
            try {
                productsCollection.document(productId).set(newProduct).await()
                productDao.markProductAsSynced(productId)
            } catch (e: Exception) {
                e.printStackTrace()
                // Không đánh dấu sản phẩm là đã đồng bộ, sẽ được đồng bộ lại sau
            }
        }

        return productId
    }

    /**
     * Xóa sản phẩm từ cả cơ sở dữ liệu cục bộ và Firebase (nếu có kết nối)
     */
    suspend fun deleteProduct(productId: String) {
        // Nếu có kết nối, xóa trên Firebase trước
        if (networkObserver.isNetworkAvailable.first()) {
            try {
                productsCollection.document(productId).delete().await()
                // Sau đó xóa khỏi cơ sở dữ liệu cục bộ
                productDao.hardDeleteProduct(productId) // Sửa từ deleteProduct -> hardDeleteProduct
            } catch (e: Exception) {
                // Nếu xóa trên Firebase thất bại, đánh dấu là đã xóa nhưng chưa đồng bộ
                e.printStackTrace()
                val product = productDao.getProductById(productId)
                if (product != null) {
                    productDao.updateProduct(product.copy(isDeleted = true, isSynced = false))
                }
            }
        } else {
            // Nếu không có kết nối, đánh dấu là đã xóa nhưng chưa đồng bộ
            val product = productDao.getProductById(productId)
            if (product != null) {
                productDao.updateProduct(product.copy(isDeleted = true, isSynced = false))
            }
        }
    }

    /**
     * Đồng bộ hóa tất cả các thay đổi cục bộ lên Firebase
     * Được gọi khi phát hiện có kết nối mạng trở lại
     */
    suspend fun syncProducts() {
        if (!networkObserver.isNetworkAvailable.first()) {
            return // Không thực hiện đồng bộ nếu không có kết nối
        }

        val unsyncedProducts = productDao.getUnsyncedProducts()

        for (product in unsyncedProducts) {
            try {
                if (product.isDeleted) {
                    // Nếu sản phẩm đã bị đánh dấu xóa cục bộ, xóa trên Firebase
                    productsCollection.document(product.id).delete().await()
                    // Sau đó xóa khỏi cơ sở dữ liệu cục bộ
                    productDao.hardDeleteProduct(product.id) // Sửa từ deleteProduct -> hardDeleteProduct
                } else {
                    // Nếu sản phẩm được t���o hoặc cập nhật, đồng bộ lên Firebase
                    productsCollection.document(product.id).set(product.toProduct()).await()
                    // Đánh dấu là đã đồng bộ
                    productDao.markProductAsSynced(product.id)
                }
            } catch (e: Exception) {
                e.printStackTrace()
                // Nếu đồng bộ thất bại, tiếp tục với sản phẩm tiếp theo
                continue
            }
        }
    }

    /**
     * Làm mới dữ liệu từ Firebase nếu có kết nối mạng
     */
    private suspend fun refreshProductsIfOnline() {
        if (networkObserver.isNetworkAvailable.first()) {
            try {
                // Lấy dữ liệu trực tiếp dưới dạng Product từ Firebase
                val remoteProducts = productsCollection.get().await().documents.mapNotNull {
                    it.toObject<Product>()?.copy(id = it.id)
                }

                // Cập nhật cơ sở dữ liệu cục bộ với dữ liệu mới nhất từ Firebase
                val productEntities = remoteProducts.map {
                    ProductEntity.fromProduct(it, true)
                }
                productDao.insertProducts(productEntities)
            } catch (e: Exception) {
                e.printStackTrace()
            }
        }
    }
}
