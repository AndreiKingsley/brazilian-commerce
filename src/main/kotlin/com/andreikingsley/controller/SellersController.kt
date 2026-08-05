package com.andreikingsley.controller

import com.andreikingsley.service.SellerService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping

@Controller
class SellersController(private val sellerService: SellerService) {

    private val rowsPerPage = 20

    @GetMapping("/sellers")
    fun sellers(
        model: Model,
        @PageableDefault(size = 20)
        pageable: Pageable,
    ): String {

        val sellersPage = sellerService.getPage(pageable)

        model.addAttribute("sellers", sellersPage)

        return "sellers_view"
    }
}
