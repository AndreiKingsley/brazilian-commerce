package com.andreikingsley.service

import com.andreikingsley.domain.Customer
import com.andreikingsley.repository.CustomerRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CustomerService(val repository: CustomerRepository) {
    @Transactional
    fun load(orders: Iterable<Customer>) {
        repository.saveAll(orders)
    }

    fun getById(customerId: String): Customer? {
        return repository.findById(customerId).map { it }.orElse(null)
    }
}