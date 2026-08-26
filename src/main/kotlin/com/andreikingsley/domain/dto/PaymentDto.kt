package com.andreikingsley.domain.dto

import com.andreikingsley.domain.Payment
import java.math.BigDecimal

data class PaymentDto(
    val paymentId: String,
    val order: OrderDto,
    val paymentInstallments: Int,
    val paymentSequential: Int,
    val paymentType: String,
    val paymentValue: BigDecimal,
)

fun Payment.toPaymentDto(): PaymentDto = PaymentDto(
    paymentId, order.toOrderDto(), paymentInstallments, paymentSequential, paymentType, paymentValue
)
