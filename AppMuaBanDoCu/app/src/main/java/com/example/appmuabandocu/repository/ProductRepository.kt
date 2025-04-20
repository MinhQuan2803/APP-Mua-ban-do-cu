package com.example.appmuabandocu.repository

import com.example.appmuabandocu.data.District
import com.example.appmuabandocu.data.Province
import com.example.appmuabandocu.data.Ward
import com.example.thuchanhapi.api.ApiService
import retrofit2.Call

class ProductRepository(private val apiService: ApiService) {
    suspend fun getProvinces(): List<Province> = apiService.getProvinces()

    suspend fun getProvinceByCode(provinceCode: String): Province =
        apiService.getProvinceByCode(provinceCode)

    suspend fun getDistrictByCode(districtCode: String): District =
        apiService.getDistrictByCode(districtCode)
}


