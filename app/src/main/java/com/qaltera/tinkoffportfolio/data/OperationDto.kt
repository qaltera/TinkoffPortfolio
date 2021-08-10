package com.qaltera.tinkoffportfolio.data

data class OperationDto (
    val id: String,
    val status: String,
    val trades: List<TradeDto>,
    val commission: MoneyAmountDto,
    val currency: String,
    val payment: Double,
    val price: Double,
    val quantity: Int,
    val quantityExecuted: Int,
    val figi: String,
    val instrumentType: String,
    val date: String,
    val operationType: String
)