package com.andreikingsley.controller

import com.andreikingsley.service.ReportService
import org.springframework.stereotype.Controller
import org.springframework.ui.Model
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestMapping

@Controller
@RequestMapping("/report")
class ReportController(
    private val reportService: ReportService
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
}