package com.andreikingsley.repository

import com.andreikingsley.domain.OrderItem
import com.andreikingsley.domain.Review
import org.springframework.data.jpa.repository.JpaRepository

interface OrderItemRepository : JpaRepository<OrderItem, String> {
    fun getAllByOrderOrderId(orderId: String): List<OrderItem>
}