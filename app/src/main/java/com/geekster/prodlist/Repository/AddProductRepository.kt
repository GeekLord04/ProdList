package com.geekster.prodlist.Repository

import com.geekster.getinfo.models.AddResponse
import com.geekster.prodlist.utils.NetworkResult
import java.io.File

interface AddProductRepository {

    suspend fun addProduct(
        productName: String,
        productType: String,
        price: String,
        tax: String,
        imageFile: File?
    ): NetworkResult<AddResponse>
}