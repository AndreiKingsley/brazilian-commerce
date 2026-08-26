package com.andreikingsley.domain.dto

import com.andreikingsley.domain.Customer
import com.andreikingsley.domain.Order
import com.andreikingsley.domain.OrderStatus
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification
import org.springframework.format.annotation.DateTimeFormat
import java.time.LocalDate

data class OrderFilter(
    val status: OrderStatus? = null,

    val customerState: String = "",

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val dateFrom: LocalDate? = null,

    @DateTimeFormat(iso = DateTimeFormat.ISO.DATE)
    val dateTo: LocalDate? = null,
)

fun OrderFilter.toSpecification(): Specification<Order> =
    Specification { root, _, cb ->
        val predicates = mutableListOf<Predicate>()

        status?.let {
            predicates += cb.equal(root.get<OrderStatus>("orderStatus"), it)
        }

        customerState.takeIf { it.isNotBlank() }?.let {
            predicates += cb.equal(
                root.get<Customer>("customer").get<String>("customerState"),
                it,
            )
        }

        dateFrom?.let {
            predicates += cb.greaterThanOrEqualTo(
                root.get("orderPurchaseTimestamp"),
                it.atStartOfDay(),
            )
        }

        dateTo?.let {
            predicates += cb.lessThan(
                root.get("orderPurchaseTimestamp"),
                it.plusDays(1).atStartOfDay(),
            )
        }

        cb.and(*predicates.toTypedArray())
    }
