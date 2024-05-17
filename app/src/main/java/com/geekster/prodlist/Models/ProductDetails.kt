package com.geekster.prodlist.Models

data class ProductDetails(
    val image: String,
    val price: Double,
    val product_name: String,
    val product_type: String,
    val tax: Double
)