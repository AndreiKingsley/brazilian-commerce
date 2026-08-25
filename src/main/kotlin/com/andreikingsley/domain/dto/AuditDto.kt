package com.andreikingsley.domain.dto

import com.andreikingsley.domain.audit.Audit
import com.andreikingsley.domain.audit.DbEntity
import jakarta.persistence.*
import java.time.Instant

data class AuditDto(
    val id: String,
    var entityId: String,
    val entityType: DbEntity,
    val field: String,
    val oldValue: String,
    val newValue: String,
    val changedAt: Instant
)

fun Audit.toAuditDto() = AuditDto(
    id = id,
    entityId = entityId,
    entityType = entityType,
    field = field,
    oldValue = oldValue,
    newValue = newValue,
    changedAt = changedAt
)