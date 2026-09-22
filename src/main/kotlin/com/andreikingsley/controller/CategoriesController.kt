package com.andreikingsley.controller

import com.andreikingsley.domain.edit.CategoryEdit
import com.andreikingsley.service.CategoryService
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
@RequestMapping("/categories")
class CategoriesController(
    private val categoryService: CategoryService,
) {

    @GetMapping
    fun categories(
        model: Model,
        @PageableDefault(size = 20)
        pageable: Pageable,
    ): String {
        val categoriesPage = categoryService.getPage(pageable)

        model.addAttribute("categories", categoriesPage)

        return "categories_view"
    }

    @GetMapping("/{id}/edit")
    fun editForm(
        @PathVariable id: String,
        @RequestParam(defaultValue = "0") page: Int,
        model: Model,
    ): String {
        val category = categoryService.getDtoByName(id)

        model.addAttribute("form", CategoryEdit(nameEn = category.productCategoryNameEnglish))

        return editView(model, id, page)
    }

    @PostMapping("/{id}/edit")
    fun editCategory(
        @PathVariable id: String,
        @RequestParam(defaultValue = "0") page: Int,
        @Valid @ModelAttribute("form") form: CategoryEdit,
        bindingResult: BindingResult,
        model: Model,
    ): String {
        if (bindingResult.hasErrors()) {
            return editView(model, id, page)
        }

        categoryService.editCategory(id, form)

        return "redirect:/categories?page=$page"
    }

    private fun editView(model: Model, id: String, page: Int): String {
        model.addAttribute("categoryId", id)
        model.addAttribute("page", page)

        return "category_edit_view"
    }

    @ResponseStatus(HttpStatus.NOT_FOUND)
    @ExceptionHandler(EntityNotFoundException::class)
    fun handleNotFound() = Unit
}
