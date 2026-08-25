package com.andreikingsley.controller

import com.andreikingsley.domain.dto.toSellerDto
import com.andreikingsley.domain.edit.SellerEdit
import com.andreikingsley.service.SellerService
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.ResponseBody

@Controller
class SellersController(private val sellerService: SellerService) {

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

    @PostMapping("/seller/{id}/edit")
    @ResponseBody
    fun editSeller(
        @PathVariable id: String,
        @Valid @RequestBody sellerEdit: SellerEdit,
        bindingResult: BindingResult,
    ): ResponseEntity<Any> {
        if (bindingResult.hasErrors()) {
            val message = bindingResult.fieldErrors.firstOrNull()?.defaultMessage
                ?: "Validation failed"
            return ResponseEntity.badRequest().body(mapOf("message" to message))
        }
        val updated = sellerService.edit(id, sellerEdit).toSellerDto()
        return ResponseEntity.ok(updated)
    }
}
