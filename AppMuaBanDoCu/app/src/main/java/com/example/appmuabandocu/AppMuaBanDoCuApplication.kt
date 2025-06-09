package com.example.appmuabandocu

import android.app.Application
import android.content.Context
import android.net.ConnectivityManager
import android.net.Network
import android.net.NetworkCapabilities
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequestBuilder
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import com.example.appmuabandocu.data.local.database.AppDatabase
import com.example.appmuabandocu.repository.ProductRepository
import com.example.appmuabandocu.utils.NetworkConnectivityObserver
import com.example.appmuabandocu.worker.SyncWorker
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.SupervisorJob
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

/**
 * Application class tùy chỉnh cho ứng dụng AppMuaBanDoCu
 * Thiết lập các thành phần cơ sở như Database, Repository và Network Observer
 */
class AppMuaBanDoCuApplication : Application() {

    // Scope cho các coroutine ở cấp ứng dụng
    private val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    // Cơ sở dữ liệu Room
    val database: AppDatabase by lazy { AppDatabase.getInstance(this) }

    // Network Observer để theo dõi trạng thái kết nối
    private val networkObserver by lazy { NetworkConnectivityObserver(this) }

    // ProductRepository
    val productRepository by lazy {
        ProductRepository(
            productDao = database.productDao(),
            firestore = FirebaseFirestore.getInstance(),
            networkObserver = networkObserver
        )
    }

    override fun onCreate() {
        super.onCreate()

        // Thiết lập theo dõi trạng thái kết nối và đồng bộ dữ liệu khi có mạng
        setupNetworkObserver()

        // Thiết lập đồng bộ định kỳ
        setupPeriodicSync()
    }

    /**
     * Theo dõi trạng thái kết nối và đồng bộ khi có kết nối mạng trở lại
     */
    private fun setupNetworkObserver() {
        applicationScope.launch {
            networkObserver.isNetworkAvailable
                // Chỉ phản ứng khi trạng thái thay đổi
                .collect { isAvailable ->
                    if (isAvailable) {
                        // Khi có kết nối mạng trở lại, tiến hành đồng bộ ngay lập tức
                        triggerImmediateSync()
                    }
                }
        }
    }

    /**
     * Gọi đồng bộ ngay lập tức khi có kết nối mạng
     */
    private fun triggerImmediateSync() {
        val syncRequest = OneTimeWorkRequestBuilder<SyncWorker>()
            .setConstraints(
                Constraints.Builder()
                    .setRequiredNetworkType(NetworkType.CONNECTED)
                    .build()
            )
            .build()

        WorkManager.getInstance(this).enqueue(syncRequest)
    }

    /**
     * Thiết lập đồng bộ định kỳ (mỗi 6 giờ) khi có kết nối mạng
     */
    private fun setupPeriodicSync() {
        val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val syncRequest = PeriodicWorkRequestBuilder<SyncWorker>(
            6, TimeUnit.HOURS // Thời gian lặp lại
        )
        .setConstraints(constraints)
        .build()

        WorkManager.getInstance(this).enqueueUniquePeriodicWork(
            "periodic_sync",
            ExistingPeriodicWorkPolicy.UPDATE,
            syncRequest
        )
    }
}
