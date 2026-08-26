package com.andreikingsley.service

import com.andreikingsley.domain.Category
import com.andreikingsley.repository.CategoryRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service

@Service
class CategoryService(private val repository: CategoryRepository) {
    @PersistenceContext
    private lateinit var em: EntityManager

    @Transactional
    fun load(categories: Iterable<Category>) {
        categories.forEach { em.persist(it) }
    }

    fun getByName(name: String): Category? {
        return repository.findById(name).map { it }.orElse(null)
    }
}
