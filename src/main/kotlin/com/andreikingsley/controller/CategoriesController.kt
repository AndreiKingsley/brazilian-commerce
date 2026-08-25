package com.andreikingsley.controller

import com.andreikingsley.domain.dto.toCategoryDto
import com.andreikingsley.domain.edit.CategoryEdit
import com.andreikingsley.service.CategoryService
import jakarta.validation.Valid
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.http.ResponseEntity
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.validation.BindingResult
import org.springframework.web.bind.annotation.*

@Controller
class CategoriesController(
    private val categoryService: CategoryService,
) {

    @GetMapping("/categories")
    fun categories(
        model: Model,
        @PageableDefault(size = 20)
        pageable: Pageable,
    ): String {
        val categoriesPage = categoryService.getPage(pageable)

        model.addAttribute("categories", categoriesPage)

        return "categories_view"
    }

    @PostMapping("/category/{id}/edit")
    @ResponseBody
    fun editCategory(
        @PathVariable id: String,
        @Valid @RequestBody categoryEdit: CategoryEdit,
        bindingResult: BindingResult,
    ): ResponseEntity<Any> {
        if (bindingResult.hasErrors()) {
            val message = bindingResult.fieldErrors.firstOrNull()?.defaultMessage
                ?: "Validation failed"
            return ResponseEntity.badRequest().body(mapOf("message" to message))
        }
        val updated = categoryService.editCategory(id, categoryEdit).toCategoryDto()
        return ResponseEntity.ok(updated)
    }
}
