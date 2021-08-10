package com.qaltera.tinkoffportfolio.composeui

import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController

@Composable
fun TinkoffPortfolioApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.Portfolio.route) {
        composable(route = Screen.Portfolio.route) {
            PortfolioScreen(
                showStockPage = { ticker ->
                    navController.navigate(Screen.Stock.createRoute(ticker))
                }
            )
        }
        composable(route = Screen.Stock.route) { backStackEntry ->
            val stockId = backStackEntry.arguments?.getString("stockId")
            requireNotNull(stockId) { "stockId parameter wasn't found. Please make sure it's set!" }
            PositionScreen(
                stockId,
                navigateUp = { navController.popBackStack(Screen.Stock.route,
                    inclusive = true) }
            )
        }
    }
}