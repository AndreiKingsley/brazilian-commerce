package com.andreikingsley.service

import com.andreikingsley.domain.Payment
import com.andreikingsley.repository.PaymentRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class PaymentService(val repository: PaymentRepository) {
    @Transactional
    fun load(orders: Iterable<Payment>) {
        repository.saveAll(orders)
    }
}