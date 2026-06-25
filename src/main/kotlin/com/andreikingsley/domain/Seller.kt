package com.andreikingsley.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "sellers")
class Seller(
    @Id
    @Column(name = "seller_id")
    var sellerId: String,

    @Column(name = "seller_city")
    var sellerCity: String,

    @Column(name = "seller_state")
    var sellerState: String,

    @Column(name = "seller_zip_code_prefix")
    var sellerZipCodePrefix: Int
)