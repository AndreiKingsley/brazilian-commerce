package com.andreikingsley.domain

import jakarta.persistence.*

@Entity
@Table(name = "reviews")
class Review(
    @Id
    @Column(name = "review_id")
    var reviewId: String,

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "order_id")
    var order: Order,

    @Column(name = "review_answer_timestamp")
    var reviewAnswerTimestamp: java.time.LocalDateTime,

    @Column(name = "review_comment_message")
    var reviewCommentMessage: String?,

    @Column(name = "review_comment_title")
    var reviewCommentTitle: String?,

    @Column(name = "review_creation_date")
    var reviewCreationDate: java.time.LocalDateTime,

    @Column(name = "review_score")
    var reviewScore: Int
)