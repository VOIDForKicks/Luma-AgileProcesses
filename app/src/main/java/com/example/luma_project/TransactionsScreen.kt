package com.example.luma_project

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale

// ---------- Model ----------

data class Transaction(
    val title: String,
    val category: String,
    val amount: Double,
    val day: Int,
    val month: String = "Oct",
    val isIncome: Boolean,
)

// Placeholder data (same entries as the Income and Expenses screens)
val sampleTransactions = listOf(
    Transaction("Monthly Salary", "Employment", 3450.00, 1, isIncome = true),
    Transaction("Freelance Project", "Side Hustle", 650.00, 12, isIncome = true),
    Transaction("Dividend Payout", "Investments", 75.50, 19, isIncome = true),
    Transaction("Supermarket", "Groceries", 64.50, 24, isIncome = false),
    Transaction("Electric Bill", "Utilities", 85.00, 22, isIncome = false),
    Transaction("Coffee Shop", "Food & Drink", 4.20, 21, isIncome = false),
    Transaction("Public Transport", "Travel", 35.00, 20, isIncome = false),
    Transaction("Online Shopping", "Retail", 49.99, 18, isIncome = false),
    Transaction("Restaurant", "Food & Drink", 52.00, 15, isIncome = false),
)

// ---------- Colors ----------

private val PurpleAccent = Color(0xFF6750A4)
private val RowBackground = Color(0xFFFDF7FF)
private val RowBorder = Color(0xFFCAC4D0)
private val AvatarBackground = Color(0xFFE8DEF8)
private val AvatarText = Color(0xFF4F378B)
private val IncomeGreen = Color(0xFF2E7D32)
private val ExpenseRed = Color(0xFFC62828)
private val TextPrimary = Color(0xFF1D1B20)
private val TextGrey = Color(0xFF888888)

// ---------- Screen ----------

@Composable
fun TransactionsScreen(
    transactions: List<Transaction> = sampleTransactions,
    onAddClick: () -> Unit = {},
    onNavigateToInsights: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
) {
    val tabs = listOf("All", "Expenses", "Income", "AI Overview")
    var selectedTab by remember { mutableIntStateOf(0) }
    var query by remember { mutableStateOf("") }

    val visible = transactions
        .filter {
            when (selectedTab) {
                1 -> !it.isIncome
                2 -> it.isIncome
                else -> true
            }
        }
        .filter {
            query.isBlank() ||
                    it.title.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)
        }
        .sortedByDescending { it.day } // most recent first

    Scaffold(
        containerColor = Color.White,
        bottomBar = { AppBottomBar("transactions", onNavigate) },
        floatingActionButton = {
            FloatingActionButton(
                onClick = onAddClick,
                containerColor = PurpleAccent,
                contentColor = Color.White,
            ) {
                Icon(Icons.Filled.Add, contentDescription = "Add transaction")
            }
        },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            // Tabs
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = PurpleAccent,
            ) {
                tabs.forEachIndexed { index, label ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            // AI Overview opens the Insights screen
                            if (index == 3) onNavigateToInsights() else selectedTab = index
                        },
                        text = { Text(label, fontSize = 13.sp) },
                    )
                }
            }

            // Search
            OutlinedTextField(
                value = query,
                onValueChange = { query = it },
                placeholder = { Text("Search", fontSize = 14.sp) },
                trailingIcon = { Icon(Icons.Outlined.Search, contentDescription = "Search") },
                singleLine = true,
                shape = RoundedCornerShape(50),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = PurpleAccent,
                    unfocusedBorderColor = RowBorder,
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
            )

            if (visible.isEmpty()) {
                Box(
                    modifier = Modifier.fillMaxSize(),
                    contentAlignment = Alignment.TopCenter,
                ) {
                    Text(
                        text = "No transactions found",
                        color = TextGrey,
                        modifier = Modifier.padding(top = 32.dp),
                    )
                }
            } else {
                LazyColumn(
                    modifier = Modifier.fillMaxSize(),
                    contentPadding = PaddingValues(start = 16.dp, end = 16.dp, bottom = 88.dp),
                    verticalArrangement = Arrangement.spacedBy(10.dp),
                ) {
                    items(visible) { transaction ->
                        TransactionRow(transaction)
                    }
                }
            }
        }
    }
}

@Composable
private fun TransactionRow(transaction: Transaction) {
    val amountColor = if (transaction.isIncome) IncomeGreen else ExpenseRed
    val sign = if (transaction.isIncome) "+" else "-"

    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(8.dp),
        colors = CardDefaults.cardColors(containerColor = RowBackground),
        border = BorderStroke(1.dp, RowBorder),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 14.dp, vertical = 14.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Avatar with first letter
            Box(
                modifier = Modifier
                    .size(38.dp)
                    .background(AvatarBackground, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = transaction.title.firstOrNull()?.uppercase() ?: "",
                    color = AvatarText,
                    fontWeight = FontWeight.Medium,
                )
            }

            Column(
                modifier = Modifier
                    .weight(1f)
                    .padding(start = 14.dp)
            ) {
                Text(
                    text = transaction.title,
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = TextPrimary,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = transaction.category,
                    fontSize = 12.sp,
                    color = TextGrey,
                )
            }

            Column(horizontalAlignment = Alignment.End) {
                Text(
                    text = sign + String.format(Locale.US, "€%,.2f", transaction.amount),
                    fontSize = 15.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = amountColor,
                )
                Spacer(Modifier.height(2.dp))
                Text(
                    text = "${transaction.month} ${"%02d".format(transaction.day)}",
                    fontSize = 12.sp,
                    color = TextGrey,
                )
            }
        }
    }
}

// ---------- Preview ----------

@Preview(showBackground = true)
@Composable
private fun TransactionsScreenPreview() {
    MaterialTheme { TransactionsScreen() }
}