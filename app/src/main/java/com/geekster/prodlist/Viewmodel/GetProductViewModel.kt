package com.geekster.prodlist.Viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekster.getinfo.models.ListResponseItem
import com.geekster.prodlist.Repository.ListProductRepositoryImpl
import com.geekster.prodlist.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class GetProductViewModel @Inject constructor(private val listProductRepository: ListProductRepositoryImpl) : ViewModel() {

    private val _listData = MutableLiveData<NetworkResult<List<ListResponseItem>>>()
    val listData: LiveData<NetworkResult<List<ListResponseItem>>> = _listData

    suspend fun getListData() {
        _listData.postValue(NetworkResult.Loading())
        val response = listProductRepository.getListData()
        _listData.postValue(response)
    }
}