package com.qaltera.tinkoffportfolio.composeui

sealed class Screen(val route: String) {
    object Portfolio: Screen("portfolio")
    object InputToken: Screen("inputToken")
    object Stock: Screen("{stockId}/details") {
        fun createRoute(stockId: String) = "$stockId/details"
    }
}