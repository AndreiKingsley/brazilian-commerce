package com.andreikingsley.repository

import com.andreikingsley.domain.Seller
import org.springframework.data.jpa.repository.JpaRepository

interface SellerRepository : JpaRepository<Seller, String>