import android.util.Log
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.lifecycle.ViewModel
import com.example.appmuabandocu.data.Product
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.ValueEventListener
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow

class ProductViewModel : ViewModel() {

    private val _productList = mutableStateListOf<Product>()
    val productList: List<Product> get() = _productList

    private val db: DatabaseReference = FirebaseDatabase.getInstance().getReference("products")

    val isLoading = mutableStateOf(false)
    val errorMessage = mutableStateOf<String?>(null)

    private val _message = MutableStateFlow("")
    val message: StateFlow<String> = _message

    init {
        loadProductsRealtime()
    }


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


    fun postProduct(product: Product) {
        val userId = FirebaseAuth.getInstance().currentUser?.uid  // Get user ID from FirebaseAuth

        if (userId != null) {
            // Add userId to product data
            val productData = product.copy(userId = userId)

            // Push the product data to Realtime Database
            val productRef = db.push()  // This will generate a unique ID for each product
            productRef.setValue(productData)
                .addOnSuccessListener {
                    Log.d("RealtimeDatabase", "Sản phẩm đã được đăng thành công!")
                    _message.value = "Sản phẩm đã được đăng thành công!"

                    // Reload the list of products after posting successfully
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

}
