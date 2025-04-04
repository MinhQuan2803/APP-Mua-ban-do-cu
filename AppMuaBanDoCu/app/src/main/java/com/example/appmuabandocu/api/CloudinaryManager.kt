//package com.example.appmuabandocu.api
//
//import com.cloudinary.Cloudinary
//import com.cloudinary.utils.ObjectUtils
//
//
//object CloudinaryManager {
//    private val config = mapOf(
//        "cloud_name" to "huynguyen",
//        "api_key" to "767613637515683",
//        "api_secret" to "kS11MCEJvwLRFMlZZUYyNfVqvy8"
//    )
//
//    val cloudinary = Cloudinary(config)
//
//    fun uploadImage(bytes: ByteArray, onResult: (String?) -> Unit) {
//        Thread {
//            try {
//                val uploadResult = cloudinary.uploader().upload(bytes, ObjectUtils.emptyMap())
//                val url = uploadResult["secure_url"] as? String
//                onResult(url)
//            } catch (e: Exception) {
//                e.printStackTrace()
//                onResult(null)
//            }
//        }.start()
//    }
//}