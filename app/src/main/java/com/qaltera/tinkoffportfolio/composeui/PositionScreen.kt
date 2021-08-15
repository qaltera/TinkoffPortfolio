package com.qaltera.tinkoffportfolio.composeui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
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
    Column {
        val positionDto = state.value.positionItem
        Text("${positionDto?.name}")
        Text("Current lots=${positionDto?.lots}")
        val lotPrice = state.value.positionTotalValue/(positionDto?.lots?.toDouble() ?: 1.0)
        Text("Current total = ${state.value.positionTotalValue.format(2)}")
        Text("Lot price = ${lotPrice.format(2)}")
        Text("fifo avg" +
            "=${state.value.positionItem?.averagePositionPrice?.value}")
        Text("Yield = ${positionDto?.expectedYield?.value}")
        Text("calculated avg=${state.value.currentAverage.format(2)}")
        Text("calculated yield=${state.value.currentYield.format(2)}")
        LazyColumn {
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
            //        Button(onClick = navigateUp) {
            //            Text("Back")
            //        }
        }
    }
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