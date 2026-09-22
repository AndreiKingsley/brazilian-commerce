package com.andreikingsley.controller

import com.andreikingsley.service.ReportService
import com.andreikingsley.service.ReportSqlService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping
import kotlin.time.measureTimedValue

@Controller
@RequestMapping("/report")
class ReportController(
    private val reportService: ReportService,
    private val reportSqlService: ReportSqlService,
) {

    @GetMapping("/revenue")
    fun revenue(model: Model): String {

        model.addAttribute("report", reportService.revenueReport())

        return "report_revenue"
    }

    @GetMapping("/geo")
    fun geo(model: Model): String {

        model.addAttribute("report", reportService.geoReport())

        return "report_geo"
    }

    @GetMapping("/delivery")
    fun delivery(model: Model): String {

        model.addAttribute("report", reportService.deliveryReport())

        return "report_delivery"
    }

    @GetMapping("/compare")
    fun compare(model: Model): String {

        val (dataFrameReport, dataFrameDuration) = measureTimedValue {
            reportService.revenueReport()
        }

        val (sqlReport, sqlDuration) = measureTimedValue {
            reportSqlService.revenueReport()
        }

        model.addAttribute("dataFrameReport", dataFrameReport)
        model.addAttribute("dataFrameDuration", dataFrameDuration)
        model.addAttribute("sqlReport", sqlReport)
        model.addAttribute("sqlDuration", sqlDuration)

        return "report_compare"
    }
}