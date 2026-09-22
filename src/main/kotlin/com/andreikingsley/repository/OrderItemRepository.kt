package com.andreikingsley.repository

import com.andreikingsley.domain.OrderItem
import com.andreikingsley.domain.report.RevenueReport
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.math.BigDecimal

interface OrderItemRepository : JpaRepository<OrderItem, String> {
    fun getAllByOrderOrderId(orderId: String): List<OrderItem>

    @Query(
        $$"""
        SELECT new com.andreikingsley.domain.report.RevenueReport$MonthlyRevenueInfo(
            monthly.salesYear,
            monthly.salesMonth,
            monthly.revenue,
            SUM(monthly.revenue) OVER (
                ORDER BY monthly.salesYear, monthly.salesMonth
            )
        )
        FROM (
            SELECT
                YEAR(o.orderPurchaseTimestamp) AS salesYear,
                MONTH(o.orderPurchaseTimestamp) AS salesMonth,
                SUM(oi.price) AS revenue
            FROM OrderItem oi
            JOIN oi.order o
            GROUP BY
                YEAR(o.orderPurchaseTimestamp),
                MONTH(o.orderPurchaseTimestamp)
        ) monthly
        ORDER BY monthly.salesYear, monthly.salesMonth
        """
    )
    fun getMonthlyRevenue(): List<RevenueReport.MonthlyRevenueInfo>

    @Query(
        """
        SELECT AVG(total) FROM (SELECT SUM(oi.price) as total
            FROM OrderItem oi
             GROUP BY oi.order 
             )
        """
    )
    fun getAverageBill(): BigDecimal

    @Query(
        $$"""
            SELECT new com.andreikingsley.domain.report.RevenueReport$SellerRevenueInfo(
            sellerId,
            total
        )
        FROM(
        select  oi.seller.sellerId as sellerId, SUM(oi.price) as total from OrderItem oi 
            group by oi.seller.sellerId
       order by total desc
       limit 10)
        """
    )
    fun getTopSellers(): List<RevenueReport.SellerRevenueInfo>

    @Query(
        $$"""
            SELECT new com.andreikingsley.domain.report.RevenueReport$CategoryRevenueInfo(
            categoryName,
            total
        )
        FROM(
        select oi.product.productCategoryName.productCategoryName as categoryName, SUM(oi.price) as total from OrderItem oi 
            group by oi.product.productCategoryName
       order by total desc 
       limit 10)
        """
    )
    fun getTopCategories(): List<RevenueReport.CategoryRevenueInfo>
}