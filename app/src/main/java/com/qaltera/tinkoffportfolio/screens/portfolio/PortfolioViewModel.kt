package com.qaltera.tinkoffportfolio.screens.portfolio

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.qaltera.tinkoffportfolio.BuildConfig
import com.qaltera.tinkoffportfolio.data.PortfolioDto
import com.qaltera.tinkoffportfolio.repository.api.Api
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.launch

class PortfolioViewModel : ViewModel() {

    val state = MutableStateFlow<PortfolioDto?>(null)
    val accountId: String? = BuildConfig.AccountId

    init {
        viewModelScope.launch {
            state.value = Api().getPortfolio(accountId).payload
        }
    }
}