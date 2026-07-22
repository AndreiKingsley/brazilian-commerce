package com.andreikingsley.repository

import com.andreikingsley.domain.Review
import org.springframework.data.jpa.repository.JpaRepository

interface ReviewRepository : JpaRepository<Review, String>