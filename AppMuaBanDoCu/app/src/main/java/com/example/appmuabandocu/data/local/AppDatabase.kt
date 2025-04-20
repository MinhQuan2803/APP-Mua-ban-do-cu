package com.example.appmuabandocu.data.local

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [ProductEntity::class], version = 1)
@TypeConverters(Converters::class)  // Đăng ký TypeConverter ở đây
abstract class AppDatabase : RoomDatabase() {
    abstract fun productDao(): ProductDao
}
