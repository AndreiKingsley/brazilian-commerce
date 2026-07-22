package com.andreikingsley.service

import com.andreikingsley.domain.Seller
import com.andreikingsley.repository.SellerRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class SellerService(val repository: SellerRepository) {
    @Transactional
    fun load(sellers: Iterable<Seller>) {
        repository.saveAll(sellers)
    }
}