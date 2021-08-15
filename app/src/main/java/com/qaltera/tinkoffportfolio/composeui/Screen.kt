package com.qaltera.tinkoffportfolio.composeui

import com.qaltera.tinkoffportfolio.data.PortfolioPositionDto
import com.qaltera.tinkoffportfolio.items.PositionItem

sealed class Screen(val route: String) {
    object Portfolio: Screen("portfolio")
    object InputToken: Screen("inputToken")
    object Stock: Screen("{figi}/details") {//TODO: ?
        fun createRoute(positionItem: PositionItem) =
            "${positionItem.figi}/details"
    }
}