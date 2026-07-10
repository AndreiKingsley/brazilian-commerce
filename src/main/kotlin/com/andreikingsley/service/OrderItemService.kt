package com.andreikingsley.service

import com.andreikingsley.domain.OrderItem
import com.andreikingsley.repository.OrderItemRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class OrderItemService(val repository: OrderItemRepository) {
    @Transactional
    fun load(orders: Iterable<OrderItem>) {
        repository.saveAll(orders)
    }
}
