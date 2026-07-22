package com.andreikingsley.controller

import com.andreikingsley.domain.dto.toSellerDto
import com.andreikingsley.service.SellerService
import org.springframework.data.domain.PageRequest
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam

@Controller
class SellersController(val sellerService: SellerService) {

    private val rowsPerPage = 20

    @GetMapping("/sellers")
    fun sellers(
        model: Model,
        @RequestParam(defaultValue = "0") page: Int
    ): String {

        val sellersPage = sellerService.repository.findAll(
            PageRequest.of(page, rowsPerPage)
        )

        model.addAttribute("sellers", sellersPage.map { it.toSellerDto() }.content)
        model.addAttribute("page", page)
        model.addAttribute("hasPrev", sellersPage.hasPrevious())
        model.addAttribute("hasNext", sellersPage.hasNext())

        return "sellers_view"
    }
}
