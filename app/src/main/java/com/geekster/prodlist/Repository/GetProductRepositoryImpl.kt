package com.geekster.prodlist.Repository

import com.geekster.getinfo.models.ListResponseItem
import com.geekster.prodlist.api.ApiService
import com.geekster.prodlist.utils.NetworkResult
import org.json.JSONObject
import javax.inject.Inject

class GetProductRepositoryImpl @Inject constructor(private val apiservice : ApiService) : GetProductRepository {

    override suspend fun getListData(): NetworkResult<List<ListResponseItem>> {
        try {
            val response = apiservice.getProducts()
            if (response.isSuccessful && response.body() != null) {
                return NetworkResult.Success(response.body()!!)
            } else if (response.errorBody() != null) {
                val errorObj = JSONObject(response.errorBody()!!.charStream().readText())
//                return NetworkResult.Error(response.errorBody().toString())
                return NetworkResult.Error(errorObj.getString("message"))
            } else {
                return NetworkResult.Error("Something went wrong!")
            }
        } catch (e: Exception) {
            return NetworkResult.Error(e.message ?: "Unknown error")
        }
    }
}