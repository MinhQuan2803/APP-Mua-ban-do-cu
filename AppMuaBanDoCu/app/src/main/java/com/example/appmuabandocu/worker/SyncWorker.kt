//package com.example.appmuabandocu.worker
//
//import android.content.Context
//import androidx.work.CoroutineWorker
//import androidx.work.WorkerParameters
//import kotlinx.coroutines.Dispatchers
//import kotlinx.coroutines.withContext
//
///**
// * Worker để đồng bộ hóa dữ liệu giữa Room Database cục bộ và Firebase
// * Được gọi định kỳ hoặc khi có kết nối mạng trở lại
// */
//class SyncWorker(
//    context: Context,
//    workerParams: WorkerParameters
//) : CoroutineWorker(context, workerParams) {
//
//    override suspend fun doWork(): Result = withContext(Dispatchers.IO) {
//        try {
//            val application = applicationContext as AppMuaBanDoCuApplication
//            val productRepository = application.productRepository
//
//            // Thực hiện đồng bộ hóa
//            productRepository.syncProducts()
//
//            Result.success()
//        } catch (e: Exception) {
//            e.printStackTrace()
//            // Thử lại sau nếu thất bại
//            Result.retry()
//        }
//    }
//}
