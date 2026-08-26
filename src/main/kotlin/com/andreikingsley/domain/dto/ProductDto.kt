package com.andreikingsley.domain.dto

import com.andreikingsley.domain.Category
import com.andreikingsley.domain.Product

data class ProductDto(
    var productId: String,
    var productCategoryName: CategoryDto?,
    var productDescriptionLenght: Int?,
    var productHeightCm: Int?,
    var productLengthCm: Int?,
    var productNameLenght: Int?,
    var productPhotosQty: Int?,
    var productWeightG: Int?,
    var productWidthCm: Int?
)

fun Product.toProductDto() = ProductDto(
    productId = productId,
    productCategoryName = productCategoryName?.toCategoryDto(),
    productDescriptionLenght = productDescriptionLenght,
    productHeightCm = productHeightCm,
    productLengthCm = productLengthCm,
    productNameLenght = productNameLenght,
    productPhotosQty = productPhotosQty,
    productWeightG = productWeightG,
    productWidthCm = productWidthCm
)
