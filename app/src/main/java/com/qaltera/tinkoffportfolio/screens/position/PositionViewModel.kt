package com.qaltera.tinkoffportfolio.screens.position

import android.util.Log
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
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
            0.0)
    )

    init {
        viewModelScope.launch {
            val operations = Api().getOperations(figi = positionDto.figi,
                from = fromDate, to = toDate).payload.operations.map {
                    OperationItem.fromDto(it)
            }
            state.value = PositionScreenState(
                operations = operations,
                calculateCurrentAvg(operations),
                positionDto
            )
        }
    }

    private fun calculateCurrentAvg(operations: List<OperationItem>)
    : Double {
        val onlyTradeOps = operations.filter { op ->
            op.status == OperationItem.Status.Done &&
                (op.operationType == OperationItem.OperationType.Buy
                    || op.operationType == OperationItem.OperationType.Sell)
        }

        var accOpValue: Double = 0.0 //nowValue
        var accLots: Int = positionDto.lots
        var accLotsBought: Int = 0
        var exitIndex = -1
        for (i in onlyTradeOps.indices) {
            val op = onlyTradeOps.get(i)
            accLots = if (op.operationType == OperationItem.OperationType.Sell)
                accLots + op.quantityExecuted
            else {
                accLotsBought += op.quantityExecuted
                accLots - op.quantityExecuted
            }
            val operationValue = op.quantityExecuted * op.price
            if (op.operationType == OperationItem.OperationType.Buy)
                accOpValue += operationValue

            Log.d("PositionVM", "i=$i accLots=$accLots acc=$accOpValue")
            if (accLots == 0) {
                Log.d("PositionVM", "breaking i=$i")
                exitIndex = i
                break
            }
        }
        val result = accOpValue/(accLotsBought)

        var accFixedProfit = 0.0
        for (i in onlyTradeOps.indices) {
            val op = onlyTradeOps.get(i)
            if (op.operationType != OperationItem.OperationType.Sell) {
                continue
            }
            if (i >= exitIndex) {
                break
            }
            accFixedProfit += (op.price - result)*op.quantityExecuted

        }

        val fixedPerLot = accFixedProfit/positionDto.lots.toDouble()
        Log.d("PositionVM", "accFixedProfit=$accFixedProfit" +
            "accFixedPerLot=$fixedPerLot result=$result")

        //return if (fixedPerLot < 0) result - fixedPerLot else result
        return result - fixedPerLot
    }

    companion object {
        //2019-08-19T18:38:33+03:00
        val FORMATTER = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
    }
}