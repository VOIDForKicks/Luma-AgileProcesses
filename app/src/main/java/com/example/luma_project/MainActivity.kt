package com.example.luma_project

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import java.util.Locale

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(modifier = Modifier.fillMaxSize()) {
                    val viewModel: FinanceViewModel = viewModel()
                    var currentScreen by remember { mutableStateOf("home") }
                    var showTransactionPanel by remember { mutableStateOf(false) }

                    val navigate: (String) -> Unit = { currentScreen = it }

                    BackHandler(enabled = currentScreen != "home") {
                        currentScreen = "home"
                    }

                    Box(modifier = Modifier.fillMaxSize()) {
                        when (currentScreen) {
                            "transactions" -> TransactionsScreen(
                                viewModel = viewModel,
                                onAddClick = { showTransactionPanel = true },
                                onNavigateToInsights = { currentScreen = "insights" },
                                onNavigate = navigate
                            )

                            "income" -> IncomeScreen(
                                viewModel = viewModel,
                                onBackClick = { currentScreen = "home" },
                                onNavigate = navigate
                            )

                            "expenses" -> ExpensesScreen(
                                viewModel = viewModel,
                                onBackClick = { currentScreen = "home" },
                                onNavigate = navigate
                            )

                            "insights" -> InsightsScreen(
                                viewModel = viewModel,
                                onNavigateToIncome = { currentScreen = "income" },
                                onNavigateToExpenses = { currentScreen = "expenses" },
                                onNavigateToAccounts = { currentScreen = "accounts" },
                                onBackClick = { currentScreen = "home" },
                                onNavigate = navigate
                            )

                            "accounts" -> AccountsScreen(
                                viewModel = viewModel,
                                onBackClick = { currentScreen = "home" },
                                onNavigate = navigate
                            )

                            else -> HomeScreen(
                                viewModel = viewModel,
                                onNavigateToIncome = { currentScreen = "income" },
                                onNavigateToExpenses = { currentScreen = "expenses" },
                                onNavigateToInsights = { currentScreen = "insights" },
                                onNavigate = navigate
                            )
                        }

                        TransactionsSidePanel(
                            visible = showTransactionPanel,
                            accounts = viewModel.user.accounts,
                            onAddTransaction = { accId, title, cat, amt, isExp, day, month, recurring ->
                                viewModel.addTransaction(accId, title, cat, amt, isExp, day, month, recurring)
                                showTransactionPanel = false
                            },
                            onDismiss = { showTransactionPanel = false }
                        )
                    }
                }
            }
        }
    }
}
