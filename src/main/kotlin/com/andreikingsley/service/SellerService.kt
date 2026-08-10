package com.andreikingsley.service

import com.andreikingsley.domain.Category
import com.andreikingsley.domain.Seller
import com.andreikingsley.domain.audit.DbEntity
import com.andreikingsley.domain.dto.CustomerDto
import com.andreikingsley.domain.dto.SellerDto
import com.andreikingsley.domain.dto.toCustomerDto
import com.andreikingsley.domain.dto.toSellerDto
import com.andreikingsley.domain.edit.CategoryEdit
import com.andreikingsley.domain.edit.SellerEdit
import com.andreikingsley.repository.SellerRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.EntityNotFoundException
import jakarta.persistence.PersistenceContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service

@Service
class SellerService(
    private val repository: SellerRepository,
    private val auditService: AuditService
) {
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

    fun Seller.updateCity(newCity: String) {
        val oldCity = sellerCity
        if (oldCity != newCity) {
            sellerCity = newCity
            auditService.audit(DbEntity.SELLER, "seller_city", oldCity, newCity)
        }
    }

    fun Seller.updateState(newState: String) {
        val oldState = sellerState
        if (oldState != newState) {
            sellerState = newState
            auditService.audit(DbEntity.SELLER, "seller_state", oldState, newState)
        }
    }

    @Transactional
    fun edit(sellerId: String, edit: SellerEdit): Seller {
        val seller = repository.findById(sellerId)
            .orElseThrow { EntityNotFoundException("Seller with ID $sellerId not found") }


        seller.updateCity(edit.city)
        seller.updateState(edit.state)

        return seller
    }
}