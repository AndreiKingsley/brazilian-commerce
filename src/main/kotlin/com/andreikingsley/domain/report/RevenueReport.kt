package com.andreikingsley.domain.report

import java.math.BigDecimal
import java.time.YearMonth

/**
 * /reports/revenue info model
 */
data class RevenueReport(
    val monthlyRevenueInfo: List<MonthlyRevenueInfo>,
    val plotRevenueHtml: String?,
    val plotRevenueCumulativeHtml: String?,
    val averageBill: BigDecimal,
    val topCategories: List<CategoryRevenueInfo>,
    val topSellers: List<SellerRevenueInfo>,
) {
    data class MonthlyRevenueInfo(val month: YearMonth, val revenue: BigDecimal, val cumulativeRevenue: BigDecimal) {
        // Used for SQL query
        @Suppress("unused")
        constructor(
            year: Int,
            month: Int,
            revenue: BigDecimal,
            cumulativeRevenue: BigDecimal,
        ) : this(
            month = YearMonth.of(year, month),
            revenue = revenue,
            cumulativeRevenue = cumulativeRevenue,
        )
    }
    data class CategoryRevenueInfo(val categoryName: String, val revenue: BigDecimal)
    data class SellerRevenueInfo(val sellerId: String, val revenue: BigDecimal)
}
