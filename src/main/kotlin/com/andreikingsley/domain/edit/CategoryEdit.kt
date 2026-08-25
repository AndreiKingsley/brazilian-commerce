package com.andreikingsley.domain.edit

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

/**
 * Form-backing bean for the category edit form: mutable properties with defaults,
 * so WebDataBinder can instantiate it and Thymeleaf can render it back via th:field.
 */
data class CategoryEdit(
    @field:NotBlank(message = "Category name can not be empty")
    @field:Size(max = 100, message = "Category name can not be contains more then 100 characters")
    var nameEn: String = ""
)
