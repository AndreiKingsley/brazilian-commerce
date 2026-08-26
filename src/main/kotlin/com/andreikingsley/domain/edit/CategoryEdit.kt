package com.andreikingsley.domain.edit

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class CategoryEdit(
    @field:NotBlank(message = "Category name can not be empty")
    @field:Size(max = 100, message = "Category name can not be contains more then 100 characters")
    var nameEn: String = ""
)
