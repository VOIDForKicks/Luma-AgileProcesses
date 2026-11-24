
package com.example.luma_project

import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.BackHandler
import androidx.activity.compose.setContent
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.google.firebase.auth.FirebaseAuth

class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        val auth = FirebaseAuth.getInstance()

        setContent {
            MaterialTheme {
                Surface(
                    modifier = Modifier.fillMaxSize()
                ) {
                    var currentScreen by remember {
                        mutableStateOf("login")
                    }

                    var showTransactionPanel by remember {
                        mutableStateOf(false)
                    }

                    var verificationEmail by remember {
                        mutableStateOf("")
                    }

                    var isLoading by remember {
                        mutableStateOf(false)
                    }

                    val navigate: (String) -> Unit = {
                        currentScreen = it
                    }

                    BackHandler(
                        enabled = (currentScreen != "home" &&
                                currentScreen != "login") ||
                                showTransactionPanel
                    ) {
                        if (showTransactionPanel) {
                            showTransactionPanel = false
                        } else if (currentScreen == "verifyEmail") {
                            currentScreen = "login"
                        } else {
                            currentScreen = "home"
                        }
                    }

                    Box(
                        modifier = Modifier.fillMaxSize()
                    ) {
                        when (currentScreen) {

                            // LOGIN SCREEN
                            "login" -> LoginScreen(
                                isLoading = isLoading,
                                onSignUp = { email, password ->

                                    isLoading = true

                                    auth.createUserWithEmailAndPassword(
                                        email.trim(),
                                        password
                                    ).addOnCompleteListener { task ->

                                        isLoading = false

                                        if (task.isSuccessful) {
                                            val user = auth.currentUser

                                            user?.sendEmailVerification()
                                                ?.addOnCompleteListener { emailTask ->

                                                    if (emailTask.isSuccessful) {
                                                        verificationEmail = email.trim()
                                                        currentScreen = "verifyEmail"
                                                    } else {
                                                        Toast.makeText(
                                                            this@MainActivity,
                                                            emailTask.exception?.message
                                                                ?: "Could not send verification email",
                                                            Toast.LENGTH_LONG
                                                        ).show()
                                                    }
                                                }
                                        } else {
                                            Toast.makeText(
                                                this@MainActivity,
                                                task.exception?.message
                                                    ?: "Registration failed",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                },
                                onLogin = { email, password ->

                                    isLoading = true

                                    auth.signInWithEmailAndPassword(
                                        email.trim(),
                                        password
                                    ).addOnCompleteListener { task ->

                                        isLoading = false

                                        if (task.isSuccessful) {
                                            val user = auth.currentUser

                                            user?.reload()
                                                ?.addOnCompleteListener { reloadTask ->

                                                    if (reloadTask.isSuccessful &&
                                                        auth.currentUser?.isEmailVerified == true
                                                    ) {
                                                        currentScreen = "home"
                                                    } else {
                                                        verificationEmail = email.trim()
                                                        currentScreen = "verifyEmail"
                                                    }
                                                }
                                        } else {
                                            Toast.makeText(
                                                this@MainActivity,
                                                task.exception?.message
                                                    ?: "Login failed",
                                                Toast.LENGTH_LONG
                                            ).show()
                                        }
                                    }
                                }
                            )

                            // EMAIL VERIFICATION SCREEN
                            "verifyEmail" -> Column(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(24.dp),
                                verticalArrangement = Arrangement.Center
                            ) {
                                Text(
                                    text = "Verify your email",
                                    style = MaterialTheme.typography.headlineMedium
                                )

                                Spacer(modifier = Modifier.height(12.dp))

                                Text(
                                    text = "We sent a verification link to $verificationEmail. Please check your inbox and spam folder."
                                )

                                Spacer(modifier = Modifier.height(24.dp))

                                Button(
                                    onClick = {
                                        auth.currentUser?.reload()
                                            ?.addOnCompleteListener { task ->

                                                if (task.isSuccessful &&
                                                    auth.currentUser?.isEmailVerified == true
                                                ) {
                                                    currentScreen = "home"
                                                } else {
                                                    Toast.makeText(
                                                        this@MainActivity,
                                                        "Your email has not been verified yet.",
                                                        Toast.LENGTH_LONG
                                                    ).show()
                                                }
                                            }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("I've verified my email")
                                }

                                Spacer(modifier = Modifier.height(8.dp))

                                TextButton(
                                    onClick = {
                                        auth.currentUser?.sendEmailVerification()
                                            ?.addOnCompleteListener { task ->
                                                val message =
                                                    if (task.isSuccessful) {
                                                        "Verification email sent again."
                                                    } else {
                                                        task.exception?.message
                                                            ?: "Could not resend email."
                                                    }

                                                Toast.makeText(
                                                    this@MainActivity,
                                                    message,
                                                    Toast.LENGTH_LONG
                                                ).show()
                                            }
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Resend verification email")
                                }

                                TextButton(
                                    onClick = {
                                        auth.signOut()
                                        currentScreen = "login"
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("Back to login")
                                }
                            }

                            // TRANSACTIONS SCREEN
                            "transactions" -> TransactionsScreen(
                                onAddClick = {
                                    showTransactionPanel = true
                                },
                                onNavigateToInsights = {
                                    currentScreen = "insights"
                                },
                                onNavigate = navigate
                            )

                            // INCOME SCREEN
                            "income" -> IncomeScreen(
                                onBackClick = {
                                    currentScreen = "home"
                                },
                                onNavigate = navigate
                            )

                            // EXPENSES SCREEN
                            "expenses" -> ExpensesScreen(
                                onBackClick = {
                                    currentScreen = "home"
                                },
                                onNavigate = navigate
                            )

                            // ADD TRANSACTION SCREEN
                            "addTransaction" -> TransactionsInfoScreen(
                                initialType = "Expense",
                                onBackClick = {
                                    currentScreen = "home"
                                }
                            )

                            // INSIGHTS SCREEN
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

                            // ACCOUNTS SCREEN
                            "accounts" -> AccountsScreen(
                                netWorth = "123,456",
                                accounts = listOf(
                                    Account("Checking", "12,345"),
                                    Account("Savings", "12,345"),
                                    Account("Credit Card", "12,345"),
                                    Account("Investments", "12,345"),
                                    Account("Cash", "12,345")
                                ),
                                onBackClick = {
                                    currentScreen = "home"
                                },
                                onNavigate = navigate
                            )

                            // HOME SCREEN
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
