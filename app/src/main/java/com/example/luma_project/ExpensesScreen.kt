package com.example.luma_project

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import java.util.Locale
import kotlin.math.abs

data class ExpenseModel(
    val title: String,
    val category: String,
    val amount: String,
    val date: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExpensesScreen(
    viewModel: FinanceViewModel,
    onBackClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
) {
    val black = Color(0xFF222222)
    val grey = Color(0xFF888888)

    val expenses = viewModel.user.accounts.flatMap { acc ->
        acc.transactions.filter { it.isExpense }.map {
            ExpenseModel(it.title, it.catagory, String.format(Locale.US, "€%,.2f", abs(it.amount)), "${it.month} ${"%02d".format(it.day)}")
        }
    }
    val totalExpensesStr = String.format(Locale.US, "€%,.2f", viewModel.totalExpenditure)

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Expense Transactions",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = black
                    )
                },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text(
                            text = "←",
                            fontSize = 22.sp,
                            color = black
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = { AppBottomBar("home", onNavigate) },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 19.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "Month-to-Date Expenses",
                fontSize = 14.sp,
                color = grey
            )
            Text(
                text = totalExpensesStr,
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFFC62828)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Recent Expenses",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = black
            )

            Spacer(modifier = Modifier.height(12.dp))

            if (expenses.isEmpty()) {
                Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.TopCenter) {
                    Text("No expense transactions found", color = grey, modifier = Modifier.padding(top = 24.dp))
                }
            } else {
                LazyColumn(
                    verticalArrangement = Arrangement.spacedBy(12.dp),
                    modifier = Modifier.fillMaxSize()
                ) {
                    items(expenses) { expense ->
                        ExpenseRowItem(expense)
                    }
                }
            }
        }
    }
}

@Composable
fun ExpenseRowItem(expense: ExpenseModel) {
    val black = Color(0xFF222222)
    val grey = Color(0xFF888888)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.LightGray.copy(alpha = 0.5f),
                shape = RoundedCornerShape(7.dp)
            )
            .padding(16.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Column {
            Text(
                text = expense.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = expense.category,
                fontSize = 12.sp,
                color = grey
            )
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "-${expense.amount}",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFFC62828)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = expense.date,
                fontSize = 12.sp,
                color = grey
            )
        }
    }
}
