package com.andreikingsley.controller

import com.andreikingsley.domain.dto.toCustomerDto
import com.andreikingsley.service.CustomerService
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class CustomersController(val customerService: CustomerService) {

    private val rowsPerPage = 20

    @GetMapping("/customers")
    fun customers(
        model: Model,
        @RequestParam(defaultValue = "0") page: Int
    ): String {

        val customersPage = customerService.repository.findAll(
            PageRequest.of(page, rowsPerPage)
        )

        model.addAttribute("customers", customersPage.map { it.toCustomerDto() }.content)
        model.addAttribute("page", page)
        model.addAttribute("hasPrev", customersPage.hasPrevious())
        model.addAttribute("hasNext", customersPage.hasNext())

        return "customers_view"
    }
}
