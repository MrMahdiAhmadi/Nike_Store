package com.example.nikestore.data

data class CartItem(
    var cart_item_id: Int,
    var count: Int,
    var product: Product,
    var changeCountProgressBarIsVisible: Boolean = false
)