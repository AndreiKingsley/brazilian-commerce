package com.andreikingsley.domain

import jakarta.persistence.*

@Entity
@Table(name = "payments")
class Payment(
    @Id
    @Column("payment_id")
    var paymentId: String,

    @ManyToOne
    @JoinColumn(name = "order_id")
    var order: Order,

    @Column(name = "payment_installments")
    var paymentInstallments: Int,

    @Column(name = "payment_sequential")
    var paymentSequential: Int,

    // TODO make enum
    @Column(name = "payment_type")
    var paymentType: String,

    @Column(name = "payment_value")
    var paymentValue: Double,
)
