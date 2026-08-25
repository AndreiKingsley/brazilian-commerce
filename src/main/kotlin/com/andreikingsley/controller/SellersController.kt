package com.andreikingsley.controller

import com.andreikingsley.domain.edit.SellerEdit
import com.andreikingsley.service.SellerService
import jakarta.persistence.EntityNotFoundException
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@Controller
@RequestMapping("/sellers")
class SellersController(private val sellerService: SellerService) {

    @GetMapping
    fun sellers(
        model: Model,
        @PageableDefault(size = 20)
        pageable: Pageable,
    ): String {

        val sellersPage = sellerService.getPage(pageable)

        model.addAttribute("sellers", sellersPage)

        return "sellers_view"
    }

    @GetMapping("/{id}/edit")
    fun editForm(
        @PathVariable id: String,
        @RequestParam(defaultValue = "0") page: Int,
        model: Model,
    ): String {
        val seller = sellerService.getDtoById(id)

        model.addAttribute("form", SellerEdit(city = seller.sellerCity, state = seller.sellerState))

        return editView(model, id, page)
    }

    @PostMapping("/{id}/edit")
    fun editSeller(
        @PathVariable id: String,
        @RequestParam(defaultValue = "0") page: Int,
        @Valid @ModelAttribute("form") form: SellerEdit,
        bindingResult: BindingResult,
        model: Model,
    ): String {
        if (bindingResult.hasErrors()) {
            return editView(model, id, page)
        }

        sellerService.edit(id, form)

        return "redirect:/sellers?page=$page"
    }

    private fun editView(model: Model, id: String, page: Int): String {
        model.addAttribute("sellerId", id)
        model.addAttribute("page", page)

        return "seller_edit_view"
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleNotFound() = Unit
}
