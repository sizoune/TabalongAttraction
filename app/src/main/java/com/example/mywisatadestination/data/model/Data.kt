package com.example.mywisatadestination.data.model

data class Data(
    val kategori: Kategori,
    val lokasi: List<Lokasi>,
    val sub_kategori: List<SubKategori>
)