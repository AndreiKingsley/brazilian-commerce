package com.andreikingsley.service

import com.andreikingsley.domain.Order
import com.andreikingsley.domain.Review
import com.andreikingsley.domain.dto.OrderDto
import com.andreikingsley.domain.dto.ProductDto
import com.andreikingsley.domain.dto.ReviewDto
import com.andreikingsley.domain.dto.toOrderDto
import com.andreikingsley.domain.dto.toProductDto
import com.andreikingsley.domain.dto.toReviewDto
import com.andreikingsley.repository.ReviewRepository
import jakarta.persistence.EntityManager
import jakarta.persistence.PersistenceContext
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
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

    @Transactional(readOnly = true)
    fun getAllDto(): List<ReviewDto> {
        return repository.findAll().map { it.toReviewDto() }
    }

    private companion object {
        const val ID_CHUNK_SIZE = 1000
    }
}