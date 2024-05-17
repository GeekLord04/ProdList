package com.geekster.prodlist.api

import com.geekster.prodlist.Models.AddResponse
import com.geekster.prodlist.Models.ListResponseItem
import com.geekster.prodlist.utils.Constants.addAPI
import com.geekster.prodlist.utils.Constants.getAPI
import okhttp3.RequestBody
import retrofit2.Response
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST

interface ApiService {

    @GET(getAPI)
    suspend fun getProducts(): Response<List<ListResponseItem>>

    @POST(addAPI)
    suspend fun addProducts(@Body requestBody: RequestBody): Response<AddResponse>

}