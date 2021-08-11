package com.qaltera.tinkoffportfolio.screens.position

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qaltera.tinkoffportfolio.data.PortfolioPositionDto

class PositionViewModelFactory(private val positionDto: PortfolioPositionDto) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = PositionViewModel(positionDto)
        as T
}