package com.geekster.prodlist.Models

data class ListResponseItem(
    val image: String,
    val price: Double,
    val product_name: String,
    val product_type: String,
    val tax: Double
)