package com.andreikingsley.service

import com.andreikingsley.domain.Review
import com.andreikingsley.repository.ReviewRepository
import jakarta.transaction.Transactional
import org.springframework.stereotype.Service

@Service
class ReviewService(val repository: ReviewRepository) {
    @Transactional
    fun load(reviews: Iterable<Review>) {
        repository.saveAll(reviews)
    }
}