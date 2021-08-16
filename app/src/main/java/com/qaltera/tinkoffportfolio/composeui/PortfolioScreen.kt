package com.qaltera.tinkoffportfolio.composeui

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.IntrinsicSize
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.MaterialTheme
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qaltera.tinkoffportfolio.R
import com.qaltera.tinkoffportfolio.data.MoneyAmountDto
import com.qaltera.tinkoffportfolio.items.PositionItem
import com.qaltera.tinkoffportfolio.screens.portfolio.PortfolioViewModel

@Composable
fun PortfolioScreen(viewModel: PortfolioViewModel = viewModel(),
    showStockPage: (position: PositionItem) -> Unit) {

    val portfolio = viewModel.state.collectAsState()
    LazyColumn {
        items(portfolio.value, key = { item ->
            item.figi
        }) { position ->
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

@Preview
@Composable
fun ComposablePreview() {
    PositionCard(
        PositionItem(
        name = "Alibaba",
            averagePositionPrice = MoneyAmountDto("USD", 220.0),
            lots = 2,
            expectedYield = MoneyAmountDto("USD", 10.5),
            figi = "fasd",
            instrumentType = PositionItem.InstrumentType.Stock,
            expectedYieldPercent = 22.0,
            totalCurrentPrice = MoneyAmountDto("USD", 210.5),
            currentLotPrice = MoneyAmountDto("USD", 209.5),
            totalPaidPrice = MoneyAmountDto("USD", 219.0)
        ),
        onClick = {}

    )
}

@Composable
fun PositionCard(asset: PositionItem,
    onClick: (PositionItem) -> Unit) {
    val asset by remember {
        mutableStateOf(asset)
    }
    Column(
        Modifier
            .padding(all = 4.dp)
            .padding(start = 4.dp, end = 4.dp)
            .fillMaxWidth()
            .clickable { onClick(asset) }) {
        val currency by remember { mutableStateOf(asset.averagePositionPrice.currency) }
        Row(Modifier.fillMaxWidth().height(IntrinsicSize.Max)) {
            Text(text = asset.name,
                Modifier
                    .fillMaxWidth()
                    .padding(end = 8.dp)
                    .weight(1f),
                maxLines = 1
            )
            Text(text = "${asset.totalCurrentPrice.value.format(2)} $currency")
        }
        Row(Modifier.fillMaxWidth().height(IntrinsicSize.Max)) {
            // Lots
            Text(
                text = "${asset.lots} " + stringResource(R.string.lots),
                Modifier.padding(end = 8.dp),
                    color = MaterialTheme.colors.secondary
            )
            // Price for 1 lot
            Text(
                text = "${asset.currentLotPrice.value.format(2)} $currency",
                Modifier.padding(end = 8.dp),
                color = MaterialTheme.colors.secondary
            )
            Box(Modifier.weight(1f))
            // Total position yield
            val isPositive by remember {
                mutableStateOf(asset.expectedYield.value > 0.0)
            }
            val sign by remember { mutableStateOf(if (isPositive) "+" else "") }
            val color by remember { mutableStateOf(if (isPositive) Color.Green else Color.Red) }
            Text(
                text = "$sign${asset.expectedYield.value.format(2)} $currency",
                Modifier.padding(end = 8.dp),
                color = color
            )
            Text(
                text = "(${asset.expectedYieldPercent.format(2)}%)",
                color = color
            )
        }
    }
}

fun Double.format(digits: Int) = "%.${digits}f".format(this)