package com.example.luma_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    var currentScreen by remember { mutableStateOf("insights") }

                    when (currentScreen) {
                        "income" -> IncomeScreen(onBackClick = { currentScreen = "insights" })
                        "expenses" -> ExpensesScreen(onBackClick = { currentScreen = "insights" })
                        else -> InsightsScreen(
                            onNavigateToIncome = { currentScreen = "income" },
                            onNavigateToExpenses = { currentScreen = "expenses" },
                            onBackClick = { currentScreen = "insights" }
                        )
                    }
                }
            }
        }
    }
}
