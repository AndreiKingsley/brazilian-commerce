package com.andreikingsley.domain.dto

import com.andreikingsley.domain.OrderItem
import com.andreikingsley.domain.Seller
import java.math.BigDecimal
import java.time.LocalDateTime

class OrderItemDto(
    val orderItemId: String,
    val order: OrderDto,
    val freightValue: Double,
    val price: BigDecimal,
    val productId: String ,
    val sellerId: String,
    val shippingLimitDate: LocalDateTime // todo kotlinx datetime
)

fun OrderItem.toOrderItemDto(): OrderItemDto = OrderItemDto(
    orderItemId, order.toOrderDto(), freightValue, price, product.productId, seller.sellerId, shippingLimitDate
)