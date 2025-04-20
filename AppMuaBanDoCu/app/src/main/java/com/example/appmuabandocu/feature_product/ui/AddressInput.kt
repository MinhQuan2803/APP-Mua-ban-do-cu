package com.example.appmuabandocu.feature_product.ui

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.IconButton
import androidx.compose.material3.DropdownMenu
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowDropDown
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.appmuabandocu.data.District
import com.example.appmuabandocu.data.Province
import com.example.appmuabandocu.data.Ward

@Composable
fun AddressInput(
    provinces: List<Province>, // Danh sách các tỉnh
    districts: List<District>, // Danh sách quận/huyện
    wards: List<Ward>, // Danh sách xã/phường
    selectedProvince: Province?,
    selectedDistrict: District?,
    selectedWard: Ward?,
    onProvinceChange: (Province) -> Unit,
    onDistrictChange: (District) -> Unit,
    onWardChange: (Ward) -> Unit,
    modifier: Modifier = Modifier,
    loadDistricts: (String) -> Unit,
    loadWards: (String) -> Unit // Hàm tải xã/phường
) {
    var expandedProvince by remember { mutableStateOf(false) }
    var expandedDistrict by remember { mutableStateOf(false) }
    var expandedWard by remember { mutableStateOf(false) }

    Column(modifier = modifier.padding(vertical = 8.dp)) {
        // Tỉnh/Thành phố Dropdown
        OutlinedTextField(
            value = selectedProvince?.name ?: "Chọn tỉnh",
            onValueChange = {},
            label = { Text("Tỉnh/Thành phố") },
            readOnly = true,
            trailingIcon = {
                IconButton(onClick = { expandedProvince = !expandedProvince }) {
                    Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                }
            },
            modifier = Modifier.fillMaxWidth(),
            singleLine = true
        )
        DropdownMenu(
            expanded = expandedProvince,
            onDismissRequest = { expandedProvince = false }
        ) {
            provinces.forEach { province ->
                DropdownMenuItem(onClick = {
                    onProvinceChange(province)
                    expandedProvince = false
                    loadDistricts(province.code.toString()) // Tải quận khi tỉnh thay đổi
                }) {
                    Text(province.name)
                }
            }
        }

        // Quận/Huyện Dropdown
        if (selectedProvince != null) {
            OutlinedTextField(
                value = selectedDistrict?.name ?: "Chọn quận/huyện",
                onValueChange = {},
                label = { Text("Quận/Huyện") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { expandedDistrict = !expandedDistrict }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            DropdownMenu(
                expanded = expandedDistrict,
                onDismissRequest = { expandedDistrict = false }
            ) {
                districts.forEach { district ->
                    DropdownMenuItem(onClick = {
                        onDistrictChange(district)
                        expandedDistrict = false
                        loadDistricts(district.code.toString())
                    }) {
                        Text(district.name)
                    }
                }
            }
        }

        // Xã/Phường Dropdown
        if (selectedDistrict != null) {
            OutlinedTextField(
                value = selectedWard?.name ?: "Chọn xã/phường",
                onValueChange = {},
                label = { Text("Xã/Phường") },
                readOnly = true,
                trailingIcon = {
                    IconButton(onClick = { expandedWard = !expandedWard }) {
                        Icon(imageVector = Icons.Default.ArrowDropDown, contentDescription = null)
                    }
                },
                modifier = Modifier.fillMaxWidth(),
                singleLine = true
            )
            DropdownMenu(
                expanded = expandedWard,
                onDismissRequest = { expandedWard = false }
            ) {
                wards.forEach { ward ->
                    DropdownMenuItem(onClick = {
                        onWardChange(ward)
                        expandedWard = false
                    }) {
                        Text(ward.name)
                    }
                }
            }
        }
    }
}

