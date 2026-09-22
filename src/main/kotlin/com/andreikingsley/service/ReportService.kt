package com.andreikingsley.service

import com.andreikingsley.domain.OrderStatus
import com.andreikingsley.domain.report.DeliveryReport
import com.andreikingsley.domain.report.GeoReport
import com.andreikingsley.domain.report.RevenueReport
import org.jetbrains.kotlinx.dataframe.api.*
import org.jetbrains.kotlinx.kandy.dsl.continuous
import org.jetbrains.kotlinx.kandy.dsl.plot
import org.jetbrains.kotlinx.kandy.letsplot.export.toHTML
import org.jetbrains.kotlinx.kandy.letsplot.feature.layout
import org.jetbrains.kotlinx.kandy.letsplot.layers.area
import org.jetbrains.kotlinx.kandy.letsplot.layers.bars
import org.jetbrains.kotlinx.kandy.letsplot.layers.pie
import org.jetbrains.kotlinx.kandy.letsplot.layers.tiles
import org.jetbrains.kotlinx.kandy.letsplot.scales.guide.LegendType
import org.jetbrains.kotlinx.kandy.letsplot.style.Style
import org.jetbrains.kotlinx.kandy.util.color.Color
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional
import java.math.BigDecimal
import java.math.RoundingMode
import java.time.YearMonth
import java.time.temporal.ChronoUnit

@Service
@Transactional(readOnly = true)
class ReportService(
    private val orderService: OrderService,
    private val orderItemService: OrderItemService,
    private val productService: ProductService,
    private val reviewService: ReviewService,
) {

    fun revenueReport(): RevenueReport {
        val orderItems = orderItemService.getAllOrderItemsDto().toDataFrame()

        val revenueByMonth = orderItems
            .add("month") { YearMonth.from(order.orderPurchaseTimestamp) }
            .groupBy { month }
            .aggregate {
                price.toList().sumOf { it } into "revenue"
            }.sortBy { month }

        val plotRevenueHtml = revenueByMonth.plot {
            bars {
                x(month.convertToString())
                y(revenue)
            }
        }.toHTML()

        val cumulativeRevenueByMonth = revenueByMonth.add("cumulativeRevenue") {
            if (index() == 0) revenue else revenue + prev()!!.newValue<BigDecimal>()
        }

        val plotRevenueCumulativeHtml = cumulativeRevenueByMonth.plot {
            area {
                x(month.convertToString())
                y(cumulativeRevenue) { axis.name = "cumulative revenue" }
            }
        }.toHTML()

        val avgBill = orderItems.groupBy { expr { order.orderId } }.aggregate {
            price.toList().sumOf { it } into "total"
        }.total.toList().let { values ->
            values.sumOf { it }.divide(BigDecimal(values.size), 2, RoundingMode.HALF_UP)
        }

        val topSellers = orderItems.groupBy { sellerId }
            .aggregate {
                price.toList().sumOf { it } into "revenue"
            }.sortByDesc { revenue }.take(10)

        val categoryByProductId = productService.getDtoByIds(orderItems.productId.asIterable())
            .associate { it.productId to it.productCategoryName?.productCategoryName }

        val topCategories = orderItems.add("categoryName") { categoryByProductId[productId] }
            .groupBy { categoryName }
            .aggregate {
                price.toList().sumOf { it } into "revenue"
            }.sortByDesc { revenue }.take(10)

        return RevenueReport(
            cumulativeRevenueByMonth.toListOf(),
            plotRevenueHtml,
            plotRevenueCumulativeHtml,
            avgBill,
            topCategories.toListOf(),
            topSellers.toListOf()
        )
    }

    fun geoReport(): GeoReport {
        val orderItems = orderItemService.getAllOrderItemsDto().toDataFrame()

        val revenueByMonthGeo = orderItems
            .add("month") { YearMonth.from(order.orderPurchaseTimestamp) }
            .add("state") { order.customer.customerState }
            .groupBy { month }
            .pivot(inward = false) { state }
            .aggregate {
                price.toList().sumOf { it }
            }.sortBy { month }

        val plotGeoHeatmapHtml = revenueByMonthGeo
            .gather { allAfter { month } }.into("state", "revenue")
            .plot {
                tiles {
                    x(state)
                    y(month.convertToString())
                    fillColor(revenue) { scale = continuous(Color.hex("#80DEEA")..Color.hex("#004D40")) }
                }

                layout.size = 900 to 600
            }.toHTML()

        return GeoReport(
            GeoReport.Matrix(
                revenueByMonthGeo.columnNames().drop(1),
                revenueByMonthGeo.month.toList(),
                revenueByMonthGeo.remove { month }.rows().map { it.values() as List<BigDecimal> }
            ),
            plotGeoHeatmapHtml)
    }

    fun deliveryReport(): DeliveryReport {
        val ordersWithDelays = orderService.getAllOrdersDto().toDataFrame()
            .filter { orderStatus == OrderStatus.DELIVERED }
            .dropNulls { orderDeliveredCustomerDate }
            .convert { orderDeliveredCustomerDate and orderEstimatedDeliveryDate }.with { it.toLocalDate() }
            .add("isDelayed") { orderDeliveredCustomerDate.isAfter(orderEstimatedDeliveryDate) }
            .add("delay") {
                if (isDelayed) {
                    ChronoUnit.DAYS.between(orderEstimatedDeliveryDate, orderDeliveredCustomerDate).toInt()
                } else 0
            }

        val delaysCount = ordersWithDelays.count { it.isDelayed }
        val total = ordersWithDelays.rowsCount()
        val delayRateGlobal = delaysCount.toDouble() / total

        val delayRateChartHtml = plot {
            pie {
                slice(columnOf(delaysCount, total-delaysCount) named "deliveries")
                fillColor(listOf("Delayed", "On time"), "status")
                size = 25.0
            }
            layout.style(Style.Void)
        }.toHTML()

        val averageDelayGlobal = ordersWithDelays.delay.mean()

        val delayByState = ordersWithDelays.add("state") { customer.customerState }
            .groupBy { state }
            .aggregate {
                count { isDelayed }.toDouble() / rowsCount() into "delayRate"
            }
            .sortByDesc { delayRate }

        val delayRateByStateChartHtml = delayByState.plot {
            bars {
                x(state)
                y(delayRate)
                fillColor(delayRate) {
                    scale = continuous(Color.GREEN..Color.RED)
                    legend.type = LegendType.None
                }
            }
        }.toHTML()


        val reviews = reviewService.getAllDto().toDataFrame()
            .add("orderId") { order.orderId }
            .groupBy { orderId }
            .aggregate { max { reviewScore } into "score" }
            .select { orderId and score }

        val delayRateByScore = ordersWithDelays.join(reviews) { orderId }.groupBy { score }
            .aggregate {
                mean { delay } into "averageDelay"
            }
            .sortBy { score }

        val delayRateByScoreChartHtml = delayRateByScore.plot {
            bars {
                x(score.convertToString())
                y(averageDelay)
                fillColor(averageDelay) {
                    scale = continuous(Color.GREEN..Color.RED)
                    legend.type = LegendType.None
                }
            }
        }.toHTML()

        return DeliveryReport(
            delayRateGlobal = delayRateGlobal,
            delayRateChartHtml = delayRateChartHtml,
            averageDelayGlobal = averageDelayGlobal,
            delayRateByState = delayByState.toListOf(),
            delayRateByStateChartHtml = delayRateByStateChartHtml,
            delayRateByScore = delayRateByScore.toListOf(),
            delayRateByScoreChartHtml = delayRateByScoreChartHtml
        )
    }
}