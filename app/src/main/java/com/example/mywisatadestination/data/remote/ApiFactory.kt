package com.example.mywisatadestination.data.remote

import com.jakewharton.retrofit2.adapter.kotlin.coroutines.CoroutineCallAdapterFactory
import okhttp3.OkHttpClient
import retrofit2.Retrofit
import retrofit2.converter.moshi.MoshiConverterFactory

object ApiFactory {
    fun retrofit(): Retrofit = Retrofit.Builder()
        .client(
            OkHttpClient().newBuilder()
                .build()
        )
        .baseUrl("http://smartcity.tabalongkab.go.id/api/")
        .addConverterFactory(MoshiConverterFactory.create())
        .addCallAdapterFactory(CoroutineCallAdapterFactory())
        .build()

    val placeApi: PlaceAPi = retrofit().create(PlaceAPi::class.java)
}