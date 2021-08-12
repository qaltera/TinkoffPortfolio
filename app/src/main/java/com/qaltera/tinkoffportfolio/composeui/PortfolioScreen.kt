package com.qaltera.tinkoffportfolio.composeui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qaltera.tinkoffportfolio.screens.portfolio.PortfolioViewModel
import androidx.compose.foundation.lazy.items
import androidx.compose.ui.Modifier
import com.qaltera.tinkoffportfolio.data.PortfolioPositionDto
import kotlinx.coroutines.yield

@Composable
fun PortfolioScreen(viewModel: PortfolioViewModel = viewModel(),
    showStockPage: (position: PortfolioPositionDto) -> Unit) {

    val portfolio = viewModel.state.collectAsState()
    LazyColumn {
        portfolio.value?.positions?.let { positions ->
            items(positions) { position ->
                PositionCard(
                    position,
                    onClick = { selectedPosition ->
                        selectedPosition.figi.let {
                            Log.d("PortfolioScreen",
                            "click selectedPosition=$selectedPosition")
                            showStockPage(selectedPosition)
                        }
                    }
                )
            }
        }
    }
}

@Composable
fun PositionCard(asset: PortfolioPositionDto,
    onClick: (PortfolioPositionDto) -> Unit) {
    val totalAmount = total(asset.averagePositionPrice?.value, asset.lots) ?: 0.0
    val totalYield = asset.expectedYield?.value ?: 0.0
    val positionPrice = (totalAmount + totalYield)/asset.lots
    val res = "name=${asset.name} " +
        "lots=${asset.lots} " +
        "avg=${asset.averagePositionPrice?.value} ${asset.averagePositionPrice?.currency} " +
        " total=${totalAmount.format(2)}" +
        " positionPrice=${positionPrice.format(2)}" +
        " yield=${asset.expectedYield?.value} ${asset.expectedYield?.currency}"
    Log.d("PortfolioScreen", res)
    Text(
        text = res,
        modifier = Modifier.clickable { onClick(asset) }
    )
}

fun total(avgPrice: Double?, lots: Int): Double? {
    return if (avgPrice == null) null
    else
        (avgPrice * lots)
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)