package com.qaltera.tinkoffportfolio.screens.position

import com.qaltera.tinkoffportfolio.data.PortfolioPositionDto
import com.qaltera.tinkoffportfolio.items.OperationItem
import com.qaltera.tinkoffportfolio.items.PositionItem

/*
 * ************************************************
 * PositionScreenState
 * Date: 2021-08-11
 * ------------------------------------------------
 * Copyright (C) SPB TV AG 2007-2021 - All Rights Reserved
 * Author: Yulia Rogovaya
 * ************************************************
 */

data class PositionScreenState(
    val operations: List<OperationItem>,
    val positionTotalValue: Double,
    val currentAverage: Double,
    val currentYield: Double,
    val currentYieldPercent: Double,
    val positionItem: PositionItem? = null
)