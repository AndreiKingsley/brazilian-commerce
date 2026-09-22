package com.andreikingsley.domain.edit

import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.Pattern
import jakarta.validation.constraints.Size

data class SellerEdit(
    @field:NotBlank(message = "City name can not be empty")
    @field:Size(max = 100, message = "City name can not be contains more then 100 characters")
    var city: String = "",
    @field:Pattern(
        regexp = "[A-Z]{2}",
        message = "State must be a two-letter uppercase code",
    )
    var state: String = "",
)
