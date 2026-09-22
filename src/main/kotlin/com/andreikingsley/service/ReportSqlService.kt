package com.andreikingsley.service

import com.andreikingsley.domain.report.RevenueReport
import com.andreikingsley.repository.OrderItemRepository
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal

@Service
class ReportSqlService(
    val orderItemRepository: OrderItemRepository
) {
    @Transactional(readOnly = true)
    fun revenueReport(): RevenueReport {
        val monthlyRevenueInfo = orderItemRepository.getMonthlyRevenue()
        val averageBill = orderItemRepository.getAverageBill()
        val topCategories = orderItemRepository.getTopCategories()
        val topSellers = orderItemRepository.getTopSellers()
        return RevenueReport(
            monthlyRevenueInfo = monthlyRevenueInfo,
            plotRevenueHtml = null,
            plotRevenueCumulativeHtml = null,
            averageBill = averageBill,
            topCategories = topCategories,
            topSellers = topSellers
        )
    }
}