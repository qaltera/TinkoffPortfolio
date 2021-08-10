package com.qaltera.tinkoffportfolio.screens.position

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qaltera.tinkoffportfolio.items.OperationItem
import com.qaltera.tinkoffportfolio.repository.api.Api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Calendar

class PositionViewModel(val figi: String): ViewModel() {
    private val toDate: String
        get() = Calendar.getInstance()
        .time.let { FORMATTER.format(it) }

    private val fromDate: String
        get() = Calendar.getInstance().apply {
            add(Calendar.YEAR, -1)
        }.time.let { FORMATTER.format(it) }

    val state = MutableStateFlow<List<OperationItem>>(emptyList())

    init {
        viewModelScope.launch {
            state.value = Api().getOperations(figi = figi,
                from = fromDate, to = toDate).payload.operations.map {
                    OperationItem.fromDto(it)
            }
        }
    }

    companion object {
        //2019-08-19T18:38:33+03:00
        val FORMATTER = SimpleDateFormat("yyyy-MM-dd'T'HH:mm:ssXXX")
    }
}