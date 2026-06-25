package com.andreikingsley.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "payments")
class Payment(

    // TODO not sure if we can use order_id as payment id
    @Id
    var id: Long,

    // OneToOne???
    @ManyToOne
    @JoinColumn(name = "order_id")
    var order: Order,

    @Column(name = "payment_installments")
    var paymentInstallments: Int,

    @Column(name = "payment_sequential")
    var paymentSequential: Int,

    @Column(name = "payment_type")
    var paymentType: String,

    @Column(name = "payment_value")
    var paymentValue: Double
)