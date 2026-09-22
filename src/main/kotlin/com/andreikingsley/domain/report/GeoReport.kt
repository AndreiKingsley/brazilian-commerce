package com.andreikingsley.domain.report

import java.math.BigDecimal
import java.time.YearMonth

data class GeoReport(
    val matrix: Matrix,
    val plotGeoHeatmapHtml: String
) {
    data class Matrix(
        val states: List<String>,
        val months: List<YearMonth>,
        val revenue: List<List<BigDecimal>>
    )
}
