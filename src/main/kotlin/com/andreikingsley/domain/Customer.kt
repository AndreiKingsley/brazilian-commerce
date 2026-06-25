package com.andreikingsley.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "customers")
class Customer(

    @Id
    @Column(name = "customer_id")
    var customerId: String,

    @Column(name = "customer_city")
    var customerCity: String,

    @Column(name = "customer_state")
    var customerState: String,

    @Column(name = "customer_unique_id")
    var customerUniqueId: String,

    @Column(name = "customer_zip_code_prefix")
    var customerZipCodePrefix: Int,
)
