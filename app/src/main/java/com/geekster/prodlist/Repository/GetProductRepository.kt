package com.geekster.prodlist.Repository

import com.geekster.getinfo.models.ListResponseItem
import com.geekster.prodlist.utils.NetworkResult

interface GetProductRepository {

    suspend fun getListData() : NetworkResult<List<ListResponseItem>>
}