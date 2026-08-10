package com.andreikingsley.service

import com.andreikingsley.domain.Category
import com.andreikingsley.domain.audit.DbEntity
import com.andreikingsley.domain.dto.CategoryDto
import com.andreikingsley.domain.dto.toCategoryDto
import com.andreikingsley.domain.edit.CategoryEdit
import com.andreikingsley.repository.CategoryRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityNotFoundException
import jakarta.persistence.PersistenceContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional


@Service
class CategoryService(
    private val repository: CategoryRepository,
    private val auditService: AuditService,
) {
    @PersistenceContext
    private lateinit var em: EntityManager

    @Transactional
    fun load(categories: Iterable<Category>) {
        categories.forEach { em.persist(it) }
    }

    fun getByName(name: String): Category? {
        return repository.findById(name).map { it }.orElse(null)
    }

    @Transactional(readOnly = true)
    fun getPage(pageable: Pageable): Page<CategoryDto> {
        return repository.findAll(pageable).map { it.toCategoryDto() }
    }

    fun Category.updateName(newName: String) {
        val oldName = productCategoryNameEnglish

        if (oldName != newName) {
            productCategoryNameEnglish = newName
            auditService.audit(DbEntity.CATEGORY, "productCategoryNameEnglish", oldName, newName)
        }
    }

    @Transactional
    fun editCategory(name: String, edit: CategoryEdit): Category {
        val category = repository.findById(name)
            .orElseThrow { EntityNotFoundException("Category $name not found") }

        category.updateName(edit.nameEn)

        return category
    }
}
