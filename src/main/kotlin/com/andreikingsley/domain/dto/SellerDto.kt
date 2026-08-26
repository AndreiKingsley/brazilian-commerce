package com.andreikingsley.domain.dto

import com.andreikingsley.domain.Seller


data class SellerDto(
    val sellerId: String,
    val sellerCity: String,
    val sellerState: String,
    val sellerZipCodePrefix: Int
)

fun Seller.toSellerDto() = SellerDto(
    sellerId = sellerId,
    sellerCity = sellerCity,
    sellerState = sellerState,
    sellerZipCodePrefix = sellerZipCodePrefix
)
