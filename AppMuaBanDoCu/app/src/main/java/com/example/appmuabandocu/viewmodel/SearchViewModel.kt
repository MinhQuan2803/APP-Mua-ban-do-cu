package com.example.appmuabandocu.viewmodel


import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.appmuabandocu.model.Product
import com.google.firebase.database.*
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch


class SearchProductViewModel : ViewModel() {


    private val _allProducts = MutableStateFlow<List<Product>>(emptyList())
    val allProducts: StateFlow<List<Product>> get() = _allProducts


    private val _searchResults = MutableStateFlow<List<Product>>(emptyList())
    val searchResults: StateFlow<List<Product>> get() = _searchResults


    private val databaseRef: DatabaseReference = FirebaseDatabase.getInstance().getReference("products")


    init {
        loadAllProducts()
    }


    private fun loadAllProducts() {
        viewModelScope.launch {
            databaseRef.addValueEventListener(object : ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val productList = mutableListOf<Product>()
                    for (child in snapshot.children) {
                        val product = child.getValue(Product::class.java)
                        product?.let {
                            it.id = child.key ?: "" // Lấy ID từ key của node
                            productList.add(it)
                        }
                    }
                    _allProducts.value = productList
                    _searchResults.value = productList
                }


                override fun onCancelled(error: DatabaseError) {
                    _allProducts.value = emptyList()
                    _searchResults.value = emptyList()
                }
            })
        }
    }


    fun searchProducts(query: String) {
        val keyword = query.trim().lowercase()
        val filtered = if (keyword.isBlank()) {
            _allProducts.value
        } else {
            _allProducts.value.filter { product ->
                product.productName?.lowercase()?.contains(keyword) == true ||
                        product.address?.lowercase()?.contains(keyword) == true ||
                        product.userName?.lowercase()?.contains(keyword) == true ||
                        product.category?.lowercase()?.contains(keyword) == true
            }
        }
        _searchResults.value = filtered
    }
}
