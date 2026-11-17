
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

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    var currentScreen by remember {
                        mutableStateOf("home")
                    }

                    var showTransactionPanel by remember {
                        mutableStateOf(false)
                    }

                    val navigate: (String) -> Unit = {
                        currentScreen = it
                    }

                    // System back button
                    BackHandler(
                        enabled = currentScreen != "home" || showTransactionPanel
                    ) {
                        if (showTransactionPanel) {
                            showTransactionPanel = false
                        } else {
                            currentScreen = "home"
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        when (currentScreen) {

                            "transactions" -> TransactionsScreen(
                                onAddClick = {
                                    showTransactionPanel = true
                                },
                                onNavigateToInsights = {
                                    currentScreen = "insights"
                                },
                                onNavigate = navigate
                            )

                            "income" -> IncomeScreen(
                                onBackClick = {
                                    currentScreen = "home"
                                },
                                onNavigate = navigate
                            )

                            "expenses" -> ExpensesScreen(
                                onBackClick = {
                                    currentScreen = "home"
                                },
                                onNavigate = navigate
                            )

                            "addTransaction" -> TransactionsInfoScreen(
                                initialType = "Expense",
                                onBackClick = {
                                    currentScreen = "home"
                                }
                            )

                            "insights" -> InsightsScreen(
                                onNavigateToIncome = {
                                    currentScreen = "income"
                                },
                                onNavigateToExpenses = {
                                    currentScreen = "expenses"
                                },
                                onNavigateToAccounts = {
                                    currentScreen = "accounts"
                                },
                                onBackClick = {
                                    currentScreen = "home"
                                },
                                onNavigate = navigate
                            )

                            "accounts" -> AccountsScreen(
                                netWorth = "123,456",
                                accounts = listOf(
                                    Account("Checking", "12,345"),
                                    Account("Savings", "12,345"),
                                    Account("Credit Card", "12,345"),
                                    Account("Investments", "12,345"),
                                    Account("Cash", "12,345"),
                                ),
                                onBackClick = {
                                    currentScreen = "home"
                                },
                                onNavigate = navigate
                            )

                            else -> HomeScreen(
                                onNavigateToIncome = {
                                    currentScreen = "income"
                                },
                                onNavigateToExpenses = {
                                    currentScreen = "expenses"
                                },
                                onNavigateToInsights = {
                                    currentScreen = "insights"
                                },
                                onAddClick = {
                                    currentScreen = "addTransaction"
                                },
                                onNavigate = navigate
                            )
                        }

                        // Existing transaction side panel
                        TransactionsSidePanel(
                            visible = showTransactionPanel,
                            onDismiss = {
                                showTransactionPanel = false
                            }
                        )
                    }
                }
            }
        }
    }
}
