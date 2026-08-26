package com.andreikingsley.domain.dto

import java.math.BigDecimal

data class OrderCard(
    val order: OrderDto,
    val reviews: List<ReviewDto>,
    val payments: List<PaymentDto>,
    val items: List<OrderItemDto>,
    val totalPrice: BigDecimal
)
