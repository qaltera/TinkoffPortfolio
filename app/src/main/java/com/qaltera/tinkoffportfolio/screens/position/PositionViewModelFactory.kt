package com.qaltera.tinkoffportfolio.screens.position

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider

class PositionViewModelFactory(private val figi: String) :
    ViewModelProvider.NewInstanceFactory() {
    override fun <T : ViewModel?> create(modelClass: Class<T>): T = PositionViewModel(figi) as T
}