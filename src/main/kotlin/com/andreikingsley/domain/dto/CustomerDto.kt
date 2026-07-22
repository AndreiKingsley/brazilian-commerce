package com.andreikingsley.domain.dto

import com.andreikingsley.domain.Customer

data class CustomerDto(
    val customerId: String,
    val customerCity: String,
    val customerState: String,
    val customerUniqueId: String,
    val customerZipCodePrefix: Int,
)

fun Customer.toCustomerDto(): CustomerDto {
    return CustomerDto(customerId, customerCity, customerState, customerUniqueId, customerZipCodePrefix)
}