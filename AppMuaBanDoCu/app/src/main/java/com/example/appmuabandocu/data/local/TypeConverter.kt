package com.example.appmuabandocu.data.local

import androidx.room.TypeConverter
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class Converters {

    // Chuyển từ String (JSON) thành List<String>
    @TypeConverter
    fun fromString(value: String?): List<String> {
        val listType = object : TypeToken<List<String>>() {}.type
        return value?.let { Gson().fromJson(it, listType) } ?: emptyList()
    }

    // Chuyển từ List<String> thành String (JSON)
    @TypeConverter
    fun fromList(list: List<String>?): String {
        return Gson().toJson(list)
    }
}
