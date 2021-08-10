package com.qaltera.tinkoffportfolio.composeui

import android.util.Log
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.qaltera.tinkoffportfolio.items.OperationItem
import com.qaltera.tinkoffportfolio.screens.position.PositionViewModel
import com.qaltera.tinkoffportfolio.screens.position.PositionViewModelFactory
import java.text.SimpleDateFormat

@Composable
fun PositionScreen(figi: String,
    viewModel: PositionViewModel =
        viewModel(factory = PositionViewModelFactory(figi)),
    navigateUp: () -> Unit) {
    val operations = viewModel.state.collectAsState()
    LazyColumn {
        operations.value.let { operations ->
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