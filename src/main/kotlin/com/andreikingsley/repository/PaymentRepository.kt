package com.andreikingsley.repository

import com.andreikingsley.domain.Payment
import com.andreikingsley.domain.Review
import org.springframework.data.jpa.repository.JpaRepository

interface PaymentRepository : JpaRepository<Payment, String> {
    fun getAllByOrderOrderId(orderId: String): List<Payment>
}