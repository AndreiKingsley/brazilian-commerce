package com.andreikingsley.repository

import com.andreikingsley.domain.Category
import org.springframework.data.jpa.repository.JpaRepository

interface CategoryRepository : JpaRepository<Category, String>