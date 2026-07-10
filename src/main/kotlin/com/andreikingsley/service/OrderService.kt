package com.andreikingsley.service

import com.andreikingsley.domain.Order
import com.andreikingsley.repository.OrderRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(val repository: OrderRepository) {
    @Transactional
    fun load(orders: Iterable<Order>) {
        repository.saveAll(orders)
    }

    fun getById(orderId: String): Order? {
        return repository.findById(orderId).map { it }.orElse(null)
    }
}
