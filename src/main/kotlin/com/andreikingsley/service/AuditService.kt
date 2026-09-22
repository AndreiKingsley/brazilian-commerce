package com.andreikingsley.service

import com.andreikingsley.domain.audit.Audit
import com.andreikingsley.domain.audit.DbEntity
import com.andreikingsley.domain.dto.AuditDto
import com.andreikingsley.domain.dto.AuditFilter
import com.andreikingsley.domain.dto.toAuditDto
import com.andreikingsley.domain.dto.toSpecification
import com.andreikingsley.repository.audit.AuditRepository
import org.springframework.data.domain.Page
import org.springframework.data.domain.Pageable
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.time.Instant

@Service
class AuditService(private val repository: AuditRepository) {
    @Transactional
    fun audit(
        entity: DbEntity,
        entityId: String,
        field: String,
        oldValue: String,
        newValue: String,
    ) {
        val changedAt = Instant.now()
        repository.save(Audit(entity, entityId, field, oldValue, newValue, changedAt))
    }

    @Transactional(readOnly = true)
    fun getPage(filter: AuditFilter, pageable: Pageable): Page<AuditDto> {
        return repository.findAll(filter.toSpecification(), pageable).map { it.toAuditDto() }
    }
}
