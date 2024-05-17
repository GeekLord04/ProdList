package com.geekster.prodlist.Viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.geekster.prodlist.Models.ListResponseItem
import com.geekster.prodlist.Repository.GetProductRepositoryImpl
import com.geekster.prodlist.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import javax.inject.Inject

@HiltViewModel
class GetProductViewModel @Inject constructor(private val listProductRepository: GetProductRepositoryImpl) : ViewModel() {

    private val _listData = MutableLiveData<NetworkResult<List<ListResponseItem>>>()
    val listLiveData: LiveData<NetworkResult<List<ListResponseItem>>> = _listData

    suspend fun getListData() {
        _listData.postValue(NetworkResult.Loading())
//        val deferredJob = viewModelScope.async{
//            listProductRepository.getListData()
//        }
//        val response = deferredJob.await()
//        val response = listProductRepository.getListData()
        val response = withContext(Dispatchers.IO) {
            listProductRepository.getListData()
        }
        _listData.postValue(response)
    }
}