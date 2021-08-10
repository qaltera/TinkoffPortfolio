package com.qaltera.tinkoffportfolio.data

data class TradeDto (
    val tradeId: String,
    val date: String,
    val price: Double,
    val quantity: Int
    )