package com.andreikingsley.service

import com.andreikingsley.domain.Product
import com.andreikingsley.domain.dto.CategoryDto
import com.andreikingsley.domain.dto.ProductDto
import com.andreikingsley.domain.dto.toCategoryDto
import com.andreikingsley.domain.dto.toProductDto
import com.andreikingsley.repository.ProductRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service

@Service
class ProductService(private val repository: ProductRepository) {
    @PersistenceContext
    private lateinit var em: EntityManager

    @Transactional
    fun load(products: Iterable<Product>) {
        products.forEach { em.persist(it) }
    }

    fun getReferenceById(id: String) = repository.getReferenceById(id)

    @Transactional(readOnly = true)
    fun getDtoByIds(ids: Iterable<String>): List<ProductDto> {
        return ids.toSet()
            .chunked(ID_CHUNK_SIZE)
            .flatMap { chunk -> repository.findAllById(chunk) }
            .map { it.toProductDto() }
    }

    @Transactional(readOnly = true)
    fun getPage(pageable: Pageable): Page<ProductDto> {
        return repository.findAll(pageable).map { it.toProductDto() }
    }

    private companion object {
        const val ID_CHUNK_SIZE = 1000
    }
}