package com.andreikingsley.repository

import com.andreikingsley.domain.Customer
import org.springframework.data.jpa.repository.JpaRepository

interface CustomerRepository : JpaRepository<Customer, String>