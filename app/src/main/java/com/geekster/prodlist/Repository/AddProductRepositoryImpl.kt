package com.geekster.prodlist.Repository

import android.util.Log
import com.geekster.getinfo.models.AddResponse
import com.geekster.prodlist.api.ApiService
import com.geekster.prodlist.utils.Constants.TAG
import com.geekster.prodlist.utils.NetworkResult
import okhttp3.MediaType.Companion.toMediaTypeOrNull
import okhttp3.MultipartBody
import okhttp3.RequestBody.Companion.asRequestBody
import java.io.File
import javax.inject.Inject

class AddProductRepositoryImpl @Inject constructor(private val apiService: ApiService) : AddProductRepository {
    override suspend fun addProduct(
        productName: String,
        productType: String,
        price: String,
        tax: String,
        imageFile: File?
    ): NetworkResult<AddResponse> {
        val requestBody = MultipartBody.Builder()
            .setType(MultipartBody.FORM)
            .addFormDataPart("product_name", productName)
            .addFormDataPart("product_type", productType)
            .addFormDataPart("price", price)
            .addFormDataPart("tax", tax)

        imageFile?.let {
            val fileBody = it.asRequestBody("image/*".toMediaTypeOrNull())
            requestBody.addFormDataPart("files[]", it.name, fileBody)
        }

        val response = apiService.addProducts(requestBody.build())

        Log.d(TAG, "addProductResponse: $response")

        return if (response.isSuccessful){
            NetworkResult.Success(response.body()!!)
        }else{
            NetworkResult.Error(response.code().toString())
        }
    }

}