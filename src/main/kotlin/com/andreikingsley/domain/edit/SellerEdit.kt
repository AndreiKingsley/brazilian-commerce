package com.andreikingsley.domain.edit

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Size

data class SellerEdit(
    @field:NotBlank(message = "City name can not be empty")
    @field:Size(max = 100, message = "City name can not be contains more then 100 characters")
    val city: String,
    @field:NotBlank(message = "State name can not be empty")
    @field:Size(max = 100, message = "State name can not be contains more then 100 characters")
    val state: String,
)
