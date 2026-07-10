package com.andreikingsley.repository.dataload

import com.andreikingsley.domain.dataload.ImportJob
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Modifying
import org.springframework.data.jpa.repository.Query
import org.springframework.transaction.annotation.Transactional
import kotlin.jvm.optionals.getOrNull

interface ImportJobRepository : JpaRepository<ImportJob, String> {
    fun getStatus(tableName: String): ImportJob.ImportStatus {
        return findById(tableName).getOrNull()?.importStatus ?: ImportJob.ImportStatus.NOT_STARTED
    }

    fun markNotStarted(tableName: String) {
        save(ImportJob(tableName, ImportJob.ImportStatus.NOT_STARTED, null, null))
    }

    fun markInProgress(tableName: String) {
        val startedAt = java.time.Instant.now()
        save(ImportJob(tableName, ImportJob.ImportStatus.IN_PROGRESS, null, startedAt = startedAt))
    }

    @Transactional
    @Modifying
    @Query("UPDATE ImportJob SET importStatus = com.andreikingsley.domain.dataload.ImportJob.ImportStatus.ERROR, errorMessage = :errorMessage WHERE tableName = :tableName")
    fun markError(tableName: String, errorMessage: String)

    @Transactional
    @Modifying
    @Query("UPDATE ImportJob SET importStatus = com.andreikingsley.domain.dataload.ImportJob.ImportStatus.DONE WHERE tableName = :tableName")
    fun markDone(tableName: String)
}