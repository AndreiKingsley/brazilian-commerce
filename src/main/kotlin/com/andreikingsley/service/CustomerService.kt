package com.andreikingsley.service

import com.andreikingsley.domain.Customer
import com.andreikingsley.repository.CustomerRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class CustomerService(val repository: CustomerRepository) {
    @Transactional
    fun load(customers: Iterable<Customer>) {
        repository.saveAll(customers)
    }
}