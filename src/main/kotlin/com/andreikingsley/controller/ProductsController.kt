package com.andreikingsley.controller

import com.andreikingsley.domain.dto.toProductDto
import com.andreikingsley.service.ProductService
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class ProductsController(val productService: ProductService) {

    private val rowsPerPage = 20

    @GetMapping("/products")
    fun products(
        model: Model,
        @RequestParam(defaultValue = "0") page: Int
    ): String {

        val productsPage = productService.repository.findAll(
            PageRequest.of(page, rowsPerPage)
        )

        model.addAttribute("products", productsPage.map { it.toProductDto() }.content)
        model.addAttribute("page", page)
        model.addAttribute("hasPrev", productsPage.hasPrevious())
        model.addAttribute("hasNext", productsPage.hasNext())

        return "products_view"
    }
}
