package com.andreikingsley.repository

import com.andreikingsley.domain.Order
import org.springframework.data.jpa.repository.JpaRepository

interface OrderRepository : JpaRepository<Order, String>