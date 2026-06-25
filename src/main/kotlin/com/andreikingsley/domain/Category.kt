package com.andreikingsley.domain

import jakarta.persistence.Column
import jakarta.persistence.Entity
import jakarta.persistence.Id
import jakarta.persistence.Table

@Entity
@Table(name = "categories")
class Category(
    @Id
    @Column(name = "product_category_name")
    var productCategoryName: String,
    @Column(name = "product_category_name_english")
    var productCategoryNameEnglish: String
)