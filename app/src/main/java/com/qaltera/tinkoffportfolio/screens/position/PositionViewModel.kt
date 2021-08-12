package com.qaltera.tinkoffportfolio.screens.position

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qaltera.tinkoffportfolio.composeui.total
import com.qaltera.tinkoffportfolio.data.PortfolioPositionDto
import com.qaltera.tinkoffportfolio.items.OperationItem
import com.qaltera.tinkoffportfolio.repository.api.Api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class PositionViewModel(val positionDto: PortfolioPositionDto): ViewModel() {
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
            0.0
        )
    )

    init {
        viewModelScope.launch {
            val operations = Api().getOperations(figi = positionDto.figi,
                from = fromDate, to = toDate).payload.operations.map {
                    OperationItem.fromDto(it)
            }
            val calculatedAvg = calculateCurrentAvg(operations)
            val calculatedYield = calculateCurrentYield(calculatedAvg)
            state.value = PositionScreenState(
                operations = operations,
                calculatedAvg,
                calculatedYield,
                positionDto
            )
        }
    }

    private fun calculateCurrentYield(calculatedAvg: Double): Double {
        val totalAmount = total(positionDto.averagePositionPrice?.value, positionDto.lots) ?: 0.0
        val totalYield = positionDto.expectedYield?.value ?: 0.0
        val positionPrice = totalAmount + totalYield
        return positionPrice - positionDto.lots*calculatedAvg
    }


    private fun calculateCurrentAvg(operations: List<OperationItem>)
    : Double {
        val onlyTradeOps = operations.filter { op ->
            op.status == OperationItem.Status.Done &&
                (op.operationType == OperationItem.OperationType.Buy
                    || op.operationType == OperationItem.OperationType.Sell)
        }

        var accLots: Int = positionDto.lots
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

        return accMoneyInvested/(positionDto.lots)
    }

    companion object {
        //2019-08-19T18:38:33+03:00
        val FORMATTER = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
    }
}