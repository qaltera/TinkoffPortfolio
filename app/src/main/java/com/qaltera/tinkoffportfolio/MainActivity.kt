package com.qaltera.tinkoffportfolio

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.qaltera.tinkoffportfolio.composeui.TinkoffPortfolioApp
import com.qaltera.tinkoffportfolio.ui.theme.TinkoffPortfolioTheme

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            TinkoffPortfolioTheme {
                TinkoffPortfolioApp()
            }
        }
    }
}
