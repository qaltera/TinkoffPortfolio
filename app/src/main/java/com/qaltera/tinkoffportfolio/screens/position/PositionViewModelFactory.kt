package com.qaltera.tinkoffportfolio.screens.position

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.qaltera.tinkoffportfolio.data.PortfolioPositionDto
import com.qaltera.tinkoffportfolio.items.PositionItem

class PositionViewModelFactory(private val positionItem: PositionItem) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = PositionViewModel(positionItem)
        as T
}