package com.andreikingsley.controller

import com.andreikingsley.domain.audit.DbEntity
import com.andreikingsley.domain.dto.AuditFilter
import com.andreikingsley.service.AuditService
import org.springframework.data.domain.Pageable
import org.springframework.data.web.PageableDefault
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.ModelAttribute

@Controller
class AuditController(
    private val auditService: AuditService,
) {

    @GetMapping("/audit")
    fun audit(
        model: Model,
        @ModelAttribute("auditFilter") auditFilter: AuditFilter,
        @PageableDefault(size = 20)
        pageable: Pageable,
    ): String {
        val auditPage = auditService.getPage(auditFilter, pageable)

        model.addAttribute("audit", auditPage)
        model.addAttribute("dbEntities", DbEntity.entries)

        return "audit_view"
    }
}
