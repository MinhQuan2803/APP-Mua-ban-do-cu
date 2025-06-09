package com.example.appmuabandocu.data

data class User(
    val uid: String = "",
    val email: String = "",
    val fullName: String = "",
    val avatarUrl: String = "",
    val phoneNumber: String? = null,
    val address: String? = null,
    val province: String? = null,
    val district: String? = null,
    val ward: String? = null,
    val isAdmin: Boolean = false,
){
    fun toMap(): Map<String, Any> {
        return mapOf(
            "id" to uid,
            "email" to email,
            "name" to fullName,
            "avatarUrl" to avatarUrl,
            "phoneNumber" to (phoneNumber ?: ""),
            "address" to (address ?: ""),
            "province" to (province ?: ""),
            "district" to (district ?: ""),
            "ward" to (ward ?: ""),
        )
    }
}
