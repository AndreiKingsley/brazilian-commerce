package com.andreikingsley.domain.dto

import com.andreikingsley.domain.Order
import com.andreikingsley.domain.OrderStatus
import java.time.LocalDateTime

data class OrderDto(
    val orderId: String,
    val customer: CustomerDto,
    var orderApprovedAt: LocalDateTime?,
    var orderDeliveredCarrierDate: LocalDateTime?,
    var orderDeliveredCustomerDate: LocalDateTime?,
    var orderEstimatedDeliveryDate: LocalDateTime,
    var orderPurchaseTimestamp: LocalDateTime,
    var orderStatus: OrderStatus,
)

fun Order.toOrderDto(): OrderDto {
    return OrderDto(
        orderId,
        customer.toCustomerDto(),
        orderApprovedAt,
        orderDeliveredCarrierDate,
        orderDeliveredCustomerDate,
        orderEstimatedDeliveryDate,
        orderPurchaseTimestamp,
        orderStatus
    )
}