package com.andreikingsley.service

import com.andreikingsley.domain.Seller
import com.andreikingsley.repository.SellerRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SellerService(val repository: SellerRepository) {
    @Transactional
    fun load(orders: Iterable<Seller>) {
        repository.saveAll(orders)
    }

    fun getById(sellerId: String): Seller? {
        return repository.findById(sellerId).map { it }.orElse(null)
    }
}