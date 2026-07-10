package com.andreikingsley.domain.dataload

import jakarta.persistence.*

@Entity
@Table(name = "import_jobs")
class ImportJob(
    @Id
    @Column(name = "table_name")
    var tableName: String,

    @Enumerated(EnumType.STRING)
    @Column(name = "import_status")
    var importStatus: ImportStatus,

    @Column(name = "error_message")
    var errorMessage: String? = null,

    @Column(name = "started_at")
    var startedAt: java.time.Instant? = null
) {
    enum class ImportStatus {
        DONE,
        ERROR,
        IN_PROGRESS,
        NOT_STARTED
    }
}
