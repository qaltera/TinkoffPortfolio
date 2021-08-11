package com.qaltera.tinkoffportfolio.data

import java.io.Serializable

data class PortfolioPositionDto(
    val figi: String,
    val ticker: String?,
    val isin: String?,
    val instrumentType: String,
    val balance: Double,
    val blocked: Double?,
    val expectedYield: MoneyAmountDto?,
    val lots: Int,
    val averagePositionPrice: MoneyAmountDto?,
    val averagePositionPriceNoNkd: MoneyAmountDto?,
    val name: String?
): Serializable
