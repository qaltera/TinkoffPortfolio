package com.qaltera.tinkoffportfolio.composeui

import android.os.Bundle
import androidx.compose.runtime.Composable
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.qaltera.tinkoffportfolio.data.PortfolioPositionDto

@Composable
fun TinkoffPortfolioApp() {
    val navController = rememberNavController()
    NavHost(navController, startDestination = Screen.Portfolio.route) {
        composable(route = Screen.Portfolio.route) {
            PortfolioScreen(
                showStockPage = { positionDto ->
                    // In the source screen...
                    navController.currentBackStackEntry?.arguments =
                        Bundle().apply {
                            putSerializable("positionDto", positionDto)
                        }
                    navController.navigate(Screen.Stock.createRoute(positionDto))
                }
            )
        }
        composable(route = Screen.Stock.route) { backStackEntry ->

            val positionDto = navController.previousBackStackEntry
                ?.arguments?.getSerializable("positionDto") as
                PortfolioPositionDto
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