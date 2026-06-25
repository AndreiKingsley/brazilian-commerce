package com.andreikingsley.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.OneToMany
import jakarta.persistence.OneToOne
import jakarta.persistence.Table

@Entity
@Table(name = "orders")
class Order(
    @Id
    @Column(name = "order_id")
    var orderId: String,

    @ManyToOne
    @JoinColumn(name = "customer_id")
    var customer: Customer,

    @Column(name = "order_approved_at")
    var orderApprovedAt: java.time.LocalDateTime?,

    @Column(name = "order_delivered_carrier_date")
    var orderDeliveredCarrierDate: java.time.LocalDateTime?,

    @Column(name = "order_delivered_customer_date")
    var orderDeliveredCustomerDate: java.time.LocalDateTime?,

    @Column(name = "order_estimated_delivery_date")
    var orderEstimatedDeliveryDate: java.time.LocalDateTime,

    @Column(name = "order_purchase_timestamp")
    var orderPurchaseTimestamp: java.time.LocalDateTime,

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    var orderStatus: OrderStatus,
)