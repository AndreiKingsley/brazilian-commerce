package com.andreikingsley.service

import com.andreikingsley.domain.Payment
import com.andreikingsley.domain.dto.PaymentDto
import com.andreikingsley.domain.dto.toPaymentDto
import com.andreikingsley.repository.PaymentRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service

@Service
class PaymentService(private val repository: PaymentRepository) {
    @PersistenceContext
    private lateinit var em: EntityManager

    @Transactional
    fun load(payments: Iterable<Payment>) {
        payments.forEach { em.persist(it) }
    }

    fun getReferenceById(id: String) = repository.getReferenceById(id)

    fun getPaymentsDto(orderId: String): List<PaymentDto> {
        return repository.getAllByOrderOrderId(orderId).map { it.toPaymentDto() }
    }
}