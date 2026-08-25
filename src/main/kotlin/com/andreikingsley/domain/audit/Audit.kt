package com.andreikingsley.domain.audit

import jakarta.persistence.*
import java.time.Instant

@Entity
@Table(name = "audit")
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
