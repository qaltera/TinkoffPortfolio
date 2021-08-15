package com.qaltera.tinkoffportfolio.items

import com.qaltera.tinkoffportfolio.data.MoneyAmountDto
import com.qaltera.tinkoffportfolio.data.PortfolioPositionDto
import java.io.Serializable

/*
 * ************************************************
 * PositionItem
 * Date: 2021-08-16
 * ------------------------------------------------
 * Copyright (C) SPB TV AG 2007-2021 - All Rights Reserved
 * Author: Yulia Rogovaya
 * ************************************************
 */

data class PositionItem(
    val figi: String,
    val name: String,
    val instrumentType: InstrumentType,
    val lots: Int,
    val averagePositionPrice: MoneyAmountDto,
    val expectedYield: MoneyAmountDto,
    val expectedYieldPercent: Double,
    val currentLotPrice: MoneyAmountDto,
    val totalCurrentPrice: MoneyAmountDto,
    val totalPaidPrice: MoneyAmountDto,
): Serializable {
    enum class InstrumentType {
        Stock, Currency, Bond, Etf
    }
    companion object {
        fun fromDto(dto: PortfolioPositionDto): PositionItem {
            val averagePositionPrice = dto.averagePositionPrice  ?:
            MoneyAmountDto("USD", 0.0)
            val expectedYield = dto.expectedYield ?: MoneyAmountDto("USD", 0.0)
            val totalPrice = averagePositionPrice.value*dto.lots
            val totalYieldPercent = (expectedYield.value*100/totalPrice)
            val totalCurrentPrice = totalPrice + expectedYield.value
            val lotPrice = totalCurrentPrice/dto.lots
            return PositionItem(
                figi = dto.figi,
                name = dto.name ?: "",
                instrumentType = InstrumentType.valueOf(dto.instrumentType),
                lots = dto.lots,
                averagePositionPrice = averagePositionPrice,
                expectedYield = expectedYield,
                expectedYieldPercent = totalYieldPercent,
                currentLotPrice = MoneyAmountDto(expectedYield.currency, lotPrice),
                totalCurrentPrice = MoneyAmountDto(expectedYield.currency, totalCurrentPrice),
                totalPaidPrice = MoneyAmountDto(expectedYield.currency, totalPrice)
            )
        }

        fun total(avgPrice: Double?, lots: Int): Double {
            val result = if (avgPrice == null) null
            else
                (avgPrice * lots)
            return result ?: 0.0
        }
    }
}