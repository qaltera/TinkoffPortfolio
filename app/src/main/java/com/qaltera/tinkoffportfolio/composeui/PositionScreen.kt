package com.qaltera.tinkoffportfolio.composeui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Card
import androidx.compose.material.Divider
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qaltera.tinkoffportfolio.R
import com.qaltera.tinkoffportfolio.items.OperationItem
import com.qaltera.tinkoffportfolio.items.PositionItem
import com.qaltera.tinkoffportfolio.screens.position.PositionViewModel
import com.qaltera.tinkoffportfolio.screens.position.PositionViewModelFactory
import java.text.SimpleDateFormat

@Composable
fun PositionScreen(positionItem: PositionItem,
    viewModel: PositionViewModel =
        viewModel(factory = PositionViewModelFactory(positionItem = positionItem)),
    navigateUp: () -> Unit) {
    val state = viewModel.state.collectAsState()
    val positionItem = state.value.positionItem

    Column {
        Text(
            text = "${positionItem?.name}",
            fontSize = 18.sp,
            modifier = Modifier.align(Alignment.CenterHorizontally)
        )
        Card(Modifier.padding(all = 8.dp)) {
            Column(Modifier.padding(all = 8.dp)) {
                Text("${positionItem?.lots} " + stringResource(R.string.lots))
                Text(
                    "${state.value.currentAverage.format(2)} " +
                        "->" + positionItem?.currentLotPrice?.value?.format(2) + " " +
                        positionItem?.currentLotPrice?.currency,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
                Divider()
                Text(stringResource(id = R.string.cost_of_papers),
                modifier = Modifier.padding(top = 8.dp))
                Text(
                    positionItem?.totalCurrentPrice?.value?.format(2) + " " +
                        positionItem?.totalCurrentPrice?.currency,
                    modifier = Modifier.padding(top = 4.dp)
                )
                val isPositive =positionItem?.expectedYield?.value ?: 0.0 > 0.0

                val sign  = if (isPositive) "+" else ""
                val color = if (isPositive) Color.Green else Color.Red
                Text(
                    text = "$sign${state.value.currentYield.format(2)}" +
                        " ${positionItem?.expectedYield?.currency}"
                        +" (${state.value.currentYieldPercent.format(2)}%)", //TODO
                    color = color
                )
                Text(
                    text = "${stringResource(id = R.string.fifo)}" +
                        " $sign${positionItem?.expectedYield?.value?.format(2)}" +
                        " ${positionItem?.expectedYield?.currency}" +
                        " (${positionItem?.expectedYieldPercent?.format(2)}%)",
                    fontSize = 12.sp,
                    modifier = Modifier.padding(bottom = 8.dp)
                )
            }
        }

        LazyColumn(Modifier.padding(all = 8.dp)){
            state.value.operations.let { operations ->
                items(operations.filter {
                    (it.operationType == OperationItem.OperationType.Buy ||
                        it.operationType == OperationItem.OperationType.Sell) &&
                        it.status == OperationItem.Status.Done
                }) { operation ->
                    OperationCard(
                        operation
                    )
                }
            }

        }
    }
//    Column {
//        val positionDto = state.value.positionItem
//        Text("${positionDto?.name}")
//        Text("Current lots=${positionDto?.lots}")
//        val lotPrice = state.value.positionTotalValue/(positionDto?.lots?.toDouble() ?: 1.0)
//        Text("Current total = ${state.value.positionTotalValue.format(2)}")
//        Text("Lot price = ${lotPrice.format(2)}")
//        Text("fifo avg" +
//            "=${state.value.positionItem?.averagePositionPrice?.value}")
//        Text("Yield = ${positionDto?.expectedYield?.value}")
//        Text("calculated avg=${state.value.currentAverage.format(2)}")
//        Text("calculated yield=${state.value.currentYield.format(2)}")
//        LazyColumn {
//            state.value.operations.let { operations ->
//                items(operations.filter {
//                    (it.operationType == OperationItem.OperationType.Buy ||
//                        it.operationType == OperationItem.OperationType.Sell) &&
//                        it.status == OperationItem.Status.Done
//                }) { operation ->
//                    OperationCard(
//                        operation
//                    )
//                }
//            }
//
//        }
//    }
}

@Composable
fun OperationCard(operation: OperationItem) {
    val res = with (operation) {
        "${operationType}" +
            " quantity=${quantity} " +
            "price=${price} " +
            "$currency ${date?.let { formatter.format(date) }}"
    }
    Log.d("PortfolioScreen", res)
    Text(
        text = res
    )
}

internal val formatter = SimpleDateFormat("yyyy-MM-dd HH:mm")