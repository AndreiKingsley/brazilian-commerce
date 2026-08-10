package com.andreikingsley.domain.dto

import com.andreikingsley.domain.audit.Audit
import com.andreikingsley.domain.audit.DbEntity
import jakarta.persistence.criteria.Predicate
import org.springframework.data.jpa.domain.Specification

data class AuditFilter(
    val entityType: DbEntity? = null,
)

fun AuditFilter.toSpecification(): Specification<Audit> =
    Specification { root, _, cb ->
        val predicates = mutableListOf<Predicate>()

        entityType?.let {
            predicates += cb.equal(root.get<DbEntity>("entityType"), it)
        }

        cb.and(*predicates.toTypedArray())
    }
