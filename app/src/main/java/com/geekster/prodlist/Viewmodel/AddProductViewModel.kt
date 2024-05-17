package com.geekster.prodlist.Viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.geekster.prodlist.Models.AddResponse
import com.geekster.prodlist.Repository.AddProductRepositoryImpl
import com.geekster.prodlist.utils.NetworkResult
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import java.io.File
import javax.inject.Inject

@HiltViewModel
class AddProductViewModel @Inject constructor(private val addRepository: AddProductRepositoryImpl) : ViewModel() {

    private val _addProduct = MutableLiveData<NetworkResult<AddResponse>>()
    val addProductLiveData: LiveData<NetworkResult<AddResponse>> = _addProduct

    fun addProduct(
        productName: String,
        productType: String,
        price: String,
        tax: String,
        imageFile: File?
    ) {
        _addProduct.postValue(NetworkResult.Loading())
        viewModelScope.launch {
            val response = addRepository.addProduct(
                productName,
                productType,
                price,
                tax,
                imageFile
            )
            _addProduct.postValue(response)
        }
    }

}