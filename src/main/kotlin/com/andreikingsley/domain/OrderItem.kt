package com.andreikingsley.domain

import jakarta.persistence.*
import java.math.BigDecimal

@Entity
@Table(name = "order_items")
class OrderItem(
    @Id
    @Column(name = "order_item_id")
    var orderItemId: String,

    @ManyToOne
    @JoinColumn(name = "order_id")
    var order: Order,

    @Column(name = "freight_value")
    var freightValue: Double,

    var price: BigDecimal,

    @ManyToOne
    @JoinColumn(name = "product_id")
    var product: Product,

    @ManyToOne
    @JoinColumn(name = "seller_id")
    var seller: Seller,

    @Column(name = "shipping_limit_date")
    var shippingLimitDate: java.time.LocalDateTime // todo kotlinx datetime
)