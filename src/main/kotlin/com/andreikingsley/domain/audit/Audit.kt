package com.andreikingsley.domain.audit

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(
    name = "audit_log",
    indexes = [
        Index(name = "idx_audit_log_changed_at", columnList = "changed_at"),
        Index(name = "idx_audit_log_type_changed_at", columnList = "entity_type, changed_at"),
    ],
)
class Audit(
    @Enumerated(EnumType.STRING)
    @Column(name = "entity_type")
    var entityType: DbEntity,

    @Column(name = "entity_id")
    var entityId: String,

    @Column
    var field: String,

    @Column(name = "old_value")
    var oldValue: String,

    @Column(name = "new_value")
    var newValue: String,

    @Column(name = "changed_at")
    var changedAt: Instant
) {
    @Id
    @GeneratedValue
    @Column
    lateinit var id: String
}
