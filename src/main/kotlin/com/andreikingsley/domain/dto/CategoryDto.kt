package com.andreikingsley.domain.dto

import com.andreikingsley.domain.Category

data class CategoryDto(
    val productCategoryName: String,
    val productCategoryNameEnglish: String
)

fun Category.toCategoryDto() = CategoryDto(
    productCategoryName = productCategoryName,
    productCategoryNameEnglish = productCategoryNameEnglish
)