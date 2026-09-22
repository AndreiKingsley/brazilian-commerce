package com.andreikingsley.controller

import com.andreikingsley.service.ProductService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class ProductsController(private val productService: ProductService) {
    @GetMapping("/products")
    fun products(
        model: Model,
        @PageableDefault(size = 20)
        pageable: Pageable,
    ): String {
        val productsPage = productService.getPage(pageable)

        model.addAttribute("products", productsPage)

        return "products_view"
    }
}
