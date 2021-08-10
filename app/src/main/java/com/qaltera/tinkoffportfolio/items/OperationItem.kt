package com.qaltera.tinkoffportfolio.items

import com.qaltera.tinkoffportfolio.data.MoneyAmountDto
import com.qaltera.tinkoffportfolio.data.OperationDto
import com.qaltera.tinkoffportfolio.data.TradeDto
import java.text.SimpleDateFormat
import java.util.Date

data class OperationItem (
    val id: String,
    val status: Status?,
    val trades: List<TradeDto>?,
    val commission: MoneyAmountDto?,
    val currency: Currency,
    val payment: Double,
    val price: Double,
    val quantity: Int,
    val quantityExecuted: Int,
    val figi: String,
    val instrumentType: String,
    val date: Date?,
    val operationType: OperationType?
) {
    enum class Status {
        Done,
        Decline,
        Progress
    }

    enum class OperationType {
        Buy,
        BuyCard,
        Sell,
        BrokerCommission,
        ExchangeCommission,
        ServiceCommission,
        MarginCommission,
        OtherCommission,
        PayIn,
        PayOut,
        Tax,
        TaxLucre,
        TaxDividend,
        TaxCoupon,
        TaxBack,
        Repayment,
        PartRepayment,
        Coupon,
        Dividend,
        SecurityIn,
        SecurityOut
    }

    companion object {
        //2019-08-19T18:38:33+03:00
        val FORMATTER = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSXXX")
        val FORMATTER2 = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ss.SSSSSSXXX")

        fun fromDto(dto: OperationDto) =
            OperationItem(
                id = dto.id,
                status = Status.valueOf(dto.status),
                trades = dto.trades,
                commission = dto.commission,
                currency = Currency.valueOf(dto.currency),
                payment = dto.payment,
                price = dto.price,
                quantity = dto.quantity,
                quantityExecuted = dto.quantityExecuted,
                figi = dto.figi,
                instrumentType = dto.instrumentType,
                date = runCatching {
                    FORMATTER.parse(dto.date)
                }?.getOrNull() ?: runCatching {
                    FORMATTER2.parse(dto.date)
                }?.getOrNull(),
                operationType = OperationType.valueOf(dto.operationType)
            )
    }
}