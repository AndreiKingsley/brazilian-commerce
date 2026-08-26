package com.andreikingsley.controller

import com.andreikingsley.service.CustomerService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class CustomersController(private val customerService: CustomerService) {

    @GetMapping("/customers")
    fun customers(
        model: Model,
        @PageableDefault(size = 20)
        pageable: Pageable,
    ): String {

        val customersPage = customerService.getPage(pageable)

        model.addAttribute("customers", customersPage)

        return "customers_view"
    }
}
