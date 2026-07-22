package com.andreikingsley.domain

import jakarta.persistence.*
import java.time.LocalDateTime

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
    var orderApprovedAt: LocalDateTime?,

    @Column(name = "order_delivered_carrier_date")
    var orderDeliveredCarrierDate: LocalDateTime?,

    @Column(name = "order_delivered_customer_date")
    var orderDeliveredCustomerDate: LocalDateTime?,

    @Column(name = "order_estimated_delivery_date")
    var orderEstimatedDeliveryDate: LocalDateTime,

    @Column(name = "order_purchase_timestamp")
    var orderPurchaseTimestamp: LocalDateTime,

    @Enumerated(EnumType.STRING)
    @Column(name = "order_status")
    var orderStatus: OrderStatus,
)