package com.andreikingsley.service

import com.andreikingsley.domain.OrderItem
import com.andreikingsley.domain.dto.OrderItemDto
import com.andreikingsley.domain.dto.PaymentDto
import com.andreikingsley.domain.dto.toOrderItemDto
import com.andreikingsley.domain.dto.toPaymentDto
import com.andreikingsley.repository.OrderItemRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service

@Service
class OrderItemService(private val repository: OrderItemRepository) {
    @PersistenceContext
    private lateinit var em: EntityManager

    @Transactional
    fun load(orderItems: Iterable<OrderItem>) {
        orderItems.forEach { em.persist(it) }
    }

    fun getReferenceById(id: String) = repository.getReferenceById(id)

    fun getOrderItemsDto(orderId: String): List<OrderItemDto> {
        return repository.getAllByOrderOrderId(orderId).map { it.toOrderItemDto() }
    }
}
