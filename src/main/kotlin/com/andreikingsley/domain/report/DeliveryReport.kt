package com.andreikingsley.domain.report

data class DeliveryReport(
    val delayRateGlobal: Double,
    val delayRateChartHtml: String,
    val averageDelayGlobal: Double,
    val delayRateByState: List<StateDelaysInfo>,
    val delayRateByStateChartHtml: String,
    val delayRateByScore: List<ScoreDelayInfo>,
    val delayRateByScoreChartHtml: String,
) {
    data class StateDelaysInfo(
        val state: String,
        val delayRate: Double,
    )

    data class ScoreDelayInfo(
        val score: Int,
        val averageDelay: Double,
    )
}
