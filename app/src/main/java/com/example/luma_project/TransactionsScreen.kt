package com.example.luma_project

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.outlined.Search
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luma_project.data.Transaction
import java.util.Locale
import kotlin.math.abs

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
    viewModel: FinanceViewModel,
    onAddClick: () -> Unit = {},
    onNavigateToInsights: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
) {
    val transactions = viewModel.user.accounts.flatMap { it.transactions }
    val tabs = listOf("All", "Expenses", "Income", "AI Overview")
    var selectedTab by remember { mutableIntStateOf(0) }
    var query by remember { mutableStateOf("") }

    val visible = transactions
        .filter {
            when (selectedTab) {
                1 -> it.isExpense
                2 -> it.isIncome
                else -> true
            }
        }
        .filter {
            query.isBlank() ||
                    it.title.contains(query, ignoreCase = true) ||
                    it.category.contains(query, ignoreCase = true)
        }
        .sortedByDescending { it.day }

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
            TabRow(
                selectedTabIndex = selectedTab,
                containerColor = Color.White,
                contentColor = PurpleAccent,
            ) {
                tabs.forEachIndexed { index, label ->
                    Tab(
                        selected = selectedTab == index,
                        onClick = {
                            if (index == 3) onNavigateToInsights() else selectedTab = index
                        },
                        text = { Text(label, fontSize = 13.sp) },
                    )
                }
            }

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
                    text = sign + String.format(Locale.US, "€%,.2f", abs(transaction.amount)),
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
