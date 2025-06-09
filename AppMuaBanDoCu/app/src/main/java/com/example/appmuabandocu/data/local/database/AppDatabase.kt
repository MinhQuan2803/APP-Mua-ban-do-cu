package com.example.appmuabandocu.data.local.database

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import androidx.room.TypeConverters
import com.example.appmuabandocu.data.local.converter.StringListConverter
import com.example.appmuabandocu.data.local.dao.ProductDao
import com.example.appmuabandocu.data.local.entity.ProductEntity

/**
 * Cơ sở dữ liệu chính của ứng dụng
 */
@Database(
    entities = [ProductEntity::class],
    version = 1,
    exportSchema = false
)
@TypeConverters(StringListConverter::class)
abstract class AppDatabase : RoomDatabase() {
    /**
     * Cung cấp quyền truy cập vào DAO của sản phẩm
     */
    abstract fun productDao(): ProductDao

    companion object {
        private const val DATABASE_NAME = "appmuabandocu.db"

        @Volatile
        private var INSTANCE: AppDatabase? = null

        /**
         * Singleton pattern để lấy instance của Database
         */
        fun getInstance(context: Context): AppDatabase {
            return INSTANCE ?: synchronized(this) {
                val instance = Room.databaseBuilder(
                    context.applicationContext,
                    AppDatabase::class.java,
                    DATABASE_NAME
                )
                .fallbackToDestructiveMigration() // Trong giai đoạn phát triển, đơn giản hóa việc migration
                .build()

                INSTANCE = instance
                instance
            }
        }
    }
}
