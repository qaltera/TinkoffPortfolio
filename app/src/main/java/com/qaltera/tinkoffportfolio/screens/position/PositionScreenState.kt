package com.qaltera.tinkoffportfolio.screens.position

import com.qaltera.tinkoffportfolio.data.PortfolioPositionDto
import com.qaltera.tinkoffportfolio.items.OperationItem

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
    val currentAverage: Double,
    val positionDto: PortfolioPositionDto? = null
)