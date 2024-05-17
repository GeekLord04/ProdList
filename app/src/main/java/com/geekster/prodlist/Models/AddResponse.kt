package com.geekster.prodlist.Models


data class AddResponse(
    val message: String,
    val product_details: ProductDetails,
    val product_id: Int,
    val success: Boolean
)