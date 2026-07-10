package com.andreikingsley.service

import com.andreikingsley.domain.Product
import com.andreikingsley.repository.ProductRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ProductService(val repository: ProductRepository) {
    @Transactional
    fun load(orders: Iterable<Product>) {
        repository.saveAll(orders)
    }

    fun getById(productId: String): Product? {
        return repository.findById(productId).map { it }.orElse(null)
    }
}