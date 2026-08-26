package com.andreikingsley.service

import com.andreikingsley.domain.Order
import com.andreikingsley.domain.dto.OrderCard
import com.andreikingsley.domain.dto.OrderDto
import com.andreikingsley.domain.dto.OrderFilter
import com.andreikingsley.domain.dto.toOrderDto
import com.andreikingsley.domain.dto.toSpecification
import com.andreikingsley.repository.OrderRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class OrderService(
    private val repository: OrderRepository,
    private val reviewService: ReviewService,
    private val paymentService: PaymentService,
    private val orderItemService: OrderItemService
) {
    @PersistenceContext
    private lateinit var em: EntityManager

    @Transactional
    fun load(orders: Iterable<Order>) {
        orders.forEach { em.persist(it) }
    }

    @Transactional(readOnly = true)
    fun getPage(filter: OrderFilter, pageable: Pageable): Page<OrderDto> {
        return repository.findAll(filter.toSpecification(), pageable).map { it.toOrderDto() }
    }

    fun getReferenceById(id: String) = repository.getReferenceById(id)

    @Transactional(readOnly = true)
    fun getCard(id: String): OrderCard? {
        val order = repository.findById(id).orElse(null) ?: return null
        val reviews = reviewService.getReviewsDto(order.orderId)
        val payments = paymentService.getPaymentsDto(order.orderId)
        val orderItems = orderItemService.getOrderItemsDto(order.orderId)
        val totalPrice = payments.sumOf { it.paymentValue }
        return OrderCard(order.toOrderDto(), reviews, payments, orderItems, totalPrice)
    }
}
