package com.andreikingsley.service

import com.andreikingsley.domain.Customer
import com.andreikingsley.domain.dto.CustomerDto
import com.andreikingsley.domain.dto.OrderDto
import com.andreikingsley.domain.dto.OrderFilter
import com.andreikingsley.domain.dto.toCustomerDto
import com.andreikingsley.domain.dto.toOrderDto
import com.andreikingsley.domain.dto.toSpecification
import com.andreikingsley.repository.CustomerRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.PageRequest
import org.springframework.data.domain.Pageable
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service

@Service
class CustomerService(private val repository: CustomerRepository) {
    @PersistenceContext
    private lateinit var em: EntityManager

    @Transactional
    fun load(customers: Iterable<Customer>) {
        customers.forEach { em.persist(it) }
    }

    fun getReferenceById(id: String) = repository.getReferenceById(id)

    @Transactional(readOnly = true)
    fun getPage(pageable: Pageable): Page<CustomerDto> {
        return repository.findAll(pageable).map { it.toCustomerDto() }
    }
}