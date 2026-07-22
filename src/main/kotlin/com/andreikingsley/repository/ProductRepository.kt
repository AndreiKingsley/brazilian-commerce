package com.andreikingsley.repository

import com.andreikingsley.domain.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, String>