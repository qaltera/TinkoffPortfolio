package com.qaltera.tinkoffportfolio.composeui

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.qaltera.tinkoffportfolio.data.PortfolioPositionDto
import com.qaltera.tinkoffportfolio.items.PositionItem

@Composable
fun TinkoffPortfolioApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.Portfolio.route) {
        composable(route = Screen.Portfolio.route) {
            PortfolioScreen(
                showStockPage = { positionItem ->
                    // In the source screen...
                    navController.currentBackStackEntry?.arguments =
                        Bundle().apply {
                            putSerializable("positionItem", positionItem)
                        }
                    navController.navigate(Screen.Stock.createRoute(positionItem))
                }
            )
        }
        composable(route = Screen.Stock.route) { backStackEntry ->

            val positionDto = navController.previousBackStackEntry
                ?.arguments?.getSerializable("positionItem") as
                PositionItem
            requireNotNull(positionDto) { "stockId parameter wasn't found. Please make sure it's " +
                "set!" }
            PositionScreen(
                positionDto,
                navigateUp = { navController.popBackStack(Screen.Stock.route,
                    inclusive = true) }
            )
        }
    }
}