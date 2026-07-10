package com.andreikingsley.service

import com.andreikingsley.domain.Category
import com.andreikingsley.repository.CategoryRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CategoryService(val repository: CategoryRepository) {
    @Transactional
    fun load(orders: Iterable<Category>) {
        repository.saveAll(orders)
    }

    fun getByName(name: String): Category? {
        return repository.findById(name).map { it }.orElse(null)
    }
}
