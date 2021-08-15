package com.qaltera.tinkoffportfolio.screens.position

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope

import com.qaltera.tinkoffportfolio.items.OperationItem
import com.qaltera.tinkoffportfolio.items.PositionItem
import com.qaltera.tinkoffportfolio.repository.api.Api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class PositionViewModel(private val positionItem: PositionItem): ViewModel() {
    private val toDate: String
        get() = Calendar.getInstance()
        .time.let { FORMATTER.format(it) }

    private val fromDate: String
        get() = Calendar.getInstance().apply {
            add(Calendar.YEAR, -1)
        }.time.let { FORMATTER.format(it) }

    val state = MutableStateFlow<PositionScreenState>(
        PositionScreenState(
            emptyList(),
            0.0,
            0.0,
            0.0
        )
    )

    init {
        viewModelScope.launch {
            val operations = Api().getOperations(figi = positionItem.figi,
                from = fromDate, to = toDate).payload.operations.map {
                    OperationItem.fromDto(it)
            }
            val calculatedAvg = calculateCurrentAvg(operations)
            val positionTotalValue = calculatePositionTotalValue()
            val calculatedYield = calculateCurrentYield(
                positionTotalValue,
                calculatedAvg
            )
            state.value = PositionScreenState(
                operations = operations,
                positionTotalValue,
                calculatedAvg,
                calculatedYield,
                positionItem
            )
        }
    }

//    val positionDto = state.value.positionDto
//    Text("Current lots=${positionDto?.lots}")
//    val expY = positionDto?.expectedYield
//    val lots = positionDto?.lots?.toDouble() ?: 0.0
//    val price: Double = positionDto?.averagePositionPrice?.value ?: 0.0
//    val beginValue = lots * price
//    val total =  beginValue + (expY?.value ?: 0.0)
//    val totalAmount = total(positionDto?.averagePositionPrice?.value,
//        positionDto?.lots ?: 0) ?: 0.0
//    val totalYield = positionDto?.expectedYield?.value ?: 0.0
//    val positionPrice = (totalAmount + totalYield)/(positionDto?.lots ?: 1)

    private fun calculatePositionTotalValue(): Double {
        val totalAmount = positionItem.totalPaidPrice.value
        val totalYield = positionItem.expectedYield.value
        return totalAmount + totalYield
    }

    private fun calculateCurrentYield(positionPrice: Double, calculatedAvg: Double): Double {
        return positionPrice - positionItem.lots*calculatedAvg
    }

    private fun calculateCurrentAvg(operations: List<OperationItem>)
    : Double {
        val onlyTradeOps = operations.filter { op ->
            op.status == OperationItem.Status.Done &&
                (op.operationType == OperationItem.OperationType.Buy
                    || op.operationType == OperationItem.OperationType.Sell)
        }

        var accLots: Int = positionItem.lots
        var accMoneyInvested = 0.0
        for (i in onlyTradeOps.indices) {
            val op = onlyTradeOps.get(i)
            if (op.operationType == OperationItem.OperationType.Sell) {
                accMoneyInvested -= op.price * op.quantityExecuted
                accLots += op.quantityExecuted
            } else {
                accMoneyInvested += op.price * op.quantityExecuted
                accLots -= op.quantityExecuted
            }
            Log.d("PositionVM", "i=$i accLots=$accLots")
            if (accLots == 0) {
                Log.d("PositionVM", "breaking i=$i")
                break
            }
        }

        return accMoneyInvested/(positionItem.lots)
    }

    companion object {
        //2019-08-19T18:38:33+03:00
        val FORMATTER = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
    }
}