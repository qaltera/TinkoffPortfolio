package com.qaltera.tinkoffportfolio.screens.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qaltera.tinkoffportfolio.BuildConfig
import com.qaltera.tinkoffportfolio.items.PositionItem
import com.qaltera.tinkoffportfolio.repository.api.Api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PortfolioViewModel : ViewModel() {

    val state = MutableStateFlow<List<PositionItem>>(emptyList())
    val accountId: String? = BuildConfig.AccountId

    init {
        viewModelScope.launch {
            state.value = Api().getPortfolio(accountId).payload.positions.map {
                PositionItem.fromDto(it)
            }
        }
    }
}