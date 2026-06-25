package com.andreikingsley.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.JoinColumn
import jakarta.persistence.ManyToOne
import jakarta.persistence.Table

@Entity
@Table(name = "products")
class Product(
    @Id
    @Column(name = "product_id")
    var productId: String,

    @ManyToOne
    @JoinColumn(name = "product_category_name")
    var productCategoryName: Category,

    @Column(name = "product_description_lenght")
    var productDescriptionLenght: Int?,

    @Column(name = "product_height_cm")
    var productHeightCm: Int?,

    @Column(name = "product_length_cm")
    var productLengthCm: Int?,

    @Column(name = "product_name_lenght")
    var productNameLenght: Int?,

    @Column(name = "product_photos_qty")
    var productPhotosQty: Int?,

    @Column(name = "product_weight_g")
    var productWeightG: Int?,

    @Column(name = "product_width_cm")
    var productWidthCm: Int?
)