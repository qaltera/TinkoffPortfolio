package com.qaltera.tinkoffportfolio.composeui

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qaltera.tinkoffportfolio.data.PortfolioPositionDto
import com.qaltera.tinkoffportfolio.items.OperationItem
import com.qaltera.tinkoffportfolio.screens.position.PositionViewModel
import com.qaltera.tinkoffportfolio.screens.position.PositionViewModelFactory
import java.text.SimpleDateFormat

@Composable
fun PositionScreen(positionDto: PortfolioPositionDto,
    viewModel: PositionViewModel =
        viewModel(factory = PositionViewModelFactory(positionDto)),
    navigateUp: () -> Unit) {
    val state = viewModel.state.collectAsState()
    Column {
        val positionDto = state.value.positionDto
        Text("Current lots=${positionDto?.lots}")
        val expY = positionDto?.expectedYield
        val lots = positionDto?.lots?.toDouble() ?: 0.0
        val price: Double = positionDto?.averagePositionPrice?.value ?: 0.0
        val beginValue = lots * price
        val total =  beginValue + (expY?.value ?: 0.0)
        val totalAmount = total(positionDto?.averagePositionPrice?.value,
            positionDto?.lots ?: 0) ?: 0.0
        val totalYield = positionDto?.expectedYield?.value ?: 0.0
        val positionPrice = (totalAmount + totalYield)/(positionDto?.lots ?: 1)

        Text("Current total = $total")
        Text("Position price = ${positionPrice.format(2)}")
        Text("Yield = ${positionDto?.expectedYield?.value}")
        Text("calculated avg=${state.value.currentAverage}")
        Text("calculated yield=${state.value.currentYield}")
        Text("fifo avg" +
            "=${state.value.positionDto?.averagePositionPrice?.value}")
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