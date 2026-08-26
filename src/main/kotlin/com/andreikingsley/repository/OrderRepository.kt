package com.andreikingsley.repository

import com.andreikingsley.domain.Order
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface OrderRepository : JpaRepository<Order, String>, JpaSpecificationExecutor<Order>