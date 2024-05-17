package com.geekster.prodlist.Repository

import com.geekster.prodlist.Models.ListResponseItem
import com.geekster.prodlist.utils.NetworkResult

interface GetProductRepository {

    suspend fun getListData() : NetworkResult<List<ListResponseItem>>
}