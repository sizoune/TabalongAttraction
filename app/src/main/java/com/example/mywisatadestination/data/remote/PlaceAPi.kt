package com.example.mywisatadestination.data.remote

import com.example.mywisatadestination.data.model.ResponsePlace
import kotlinx.coroutines.Deferred
import retrofit2.Response
import retrofit2.http.POST

interface PlaceAPi {
    @POST("lokasi/2")
    fun getLokasi(): Deferred<Response<ResponsePlace>>
}