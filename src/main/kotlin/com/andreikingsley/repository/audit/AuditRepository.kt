package com.andreikingsley.repository.audit

import com.andreikingsley.domain.audit.Audit
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.JpaSpecificationExecutor

interface AuditRepository : JpaRepository<Audit, String>, JpaSpecificationExecutor<Audit>
