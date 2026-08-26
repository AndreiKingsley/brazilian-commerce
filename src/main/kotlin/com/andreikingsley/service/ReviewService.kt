package com.andreikingsley.service

import com.andreikingsley.domain.Order
import com.andreikingsley.domain.Review
import com.andreikingsley.domain.dto.ReviewDto
import com.andreikingsley.domain.dto.toReviewDto
import com.andreikingsley.repository.ReviewRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.transaction.annotation.Transactional
import org.springframework.stereotype.Service

@Service
class ReviewService(private val repository: ReviewRepository) {
    @PersistenceContext
    private lateinit var em: EntityManager

    @Transactional
    fun load(reviews: Iterable<Review>) {
        reviews.forEach { em.persist(it) }
    }

    fun getReferenceById(id: String) = repository.getReferenceById(id)

    fun getReviewsDto(orderId: String): List<ReviewDto> {
        return repository.getAllByOrderOrderId(orderId).map { it.toReviewDto() }
    }
}