package com.andreikingsley.domain.dto

import com.andreikingsley.domain.Order
import com.andreikingsley.domain.Review
import java.time.LocalDateTime

data class ReviewDto(
    val reviewId: String,
    val order: Order,
    val reviewAnswerTimestamp: LocalDateTime,
    val reviewCommentMessage: String?,
    val reviewCommentTitle: String?,
    val reviewCreationDate: LocalDateTime,
    val reviewScore: Int
)

fun Review.toReviewDto(): ReviewDto {
    return ReviewDto(
        reviewId = reviewId,
        order = order,
        reviewAnswerTimestamp = reviewAnswerTimestamp,
        reviewCommentMessage = reviewCommentMessage,
        reviewCommentTitle = reviewCommentTitle,
        reviewCreationDate = reviewCreationDate,
        reviewScore = reviewScore
    )
}
