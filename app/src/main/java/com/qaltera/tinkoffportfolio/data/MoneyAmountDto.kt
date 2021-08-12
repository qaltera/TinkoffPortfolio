package com.qaltera.tinkoffportfolio.data

import java.io.Serializable

data class MoneyAmountDto (
    val currency: String,
    val value: Double
): Serializable