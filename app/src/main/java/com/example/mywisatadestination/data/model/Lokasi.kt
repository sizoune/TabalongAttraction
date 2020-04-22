package com.example.mywisatadestination.data.model

import android.os.Parcelable
import kotlinx.android.parcel.Parcelize

@Parcelize
data class Lokasi(
    val id: Int,
    val deskripsi: String,
    val foto: String,
    val longitude: Double,
    val latitude: Double,
    val nama: String
) : Parcelable {
    companion object {
        const val TABLE_FAVORITE: String = "TABLE_FAVORITE"
        const val ID: String = "ID_"
        const val DESKRIPSI = "DESKRIPSI"
        const val FOTO = "FOTO"
        const val LATITUDE: String = "LATITUDE"
        const val LONGITUDE: String = "LONGITUDE"
        const val NAMA: String = "NAMA"
    }
}