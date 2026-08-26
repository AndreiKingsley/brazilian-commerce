package com.andreikingsley.service

import com.andreikingsley.domain.Seller
import com.andreikingsley.domain.dto.CustomerDto
import com.andreikingsley.domain.dto.SellerDto
import com.andreikingsley.domain.dto.toCustomerDto
import com.andreikingsley.domain.dto.toSellerDto
import com.andreikingsley.repository.SellerRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service

@Service
class SellerService(private val repository: SellerRepository) {
    @PersistenceContext
    private lateinit var em: EntityManager

    @Transactional
    fun load(sellers: Iterable<Seller>) {
        sellers.forEach { em.persist(it) }
    }

    fun getReferenceById(id: String) = repository.getReferenceById(id)

    @Transactional(readOnly = true)
    fun getPage(pageable: Pageable): Page<SellerDto> {
        return repository.findAll(pageable).map { it.toSellerDto() }
    }
}