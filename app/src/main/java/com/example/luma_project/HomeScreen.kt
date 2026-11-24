package com.example.luma_project

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luma_project.data.Transaction
import java.util.Locale
import kotlin.math.abs

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: FinanceViewModel,
    onNavigateToIncome: () -> Unit = {},
    onNavigateToExpenses: () -> Unit = {},
    onNavigateToInsights: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
) {
    val black = Color(0xFF222222)
    val grey = Color(0xFF888888)

    val incomeStr = String.format(Locale.US, "€%,.2f", viewModel.totalIncome)
    val balanceStr = String.format(Locale.US, "€%,.2f", viewModel.netWorth)
    val expenditureStr = String.format(Locale.US, "€%,.2f", viewModel.totalExpenditure)
    val transactions = viewModel.user.accounts.flatMap { it.transactions }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Luma",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = black
                    )
                },
                navigationIcon = {
                    Text(
                        text = "☰",
                        fontSize = 24.sp,
                        color = black,
                        modifier = Modifier.padding(start = 16.dp)
                    )
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

            Spacer(modifier = Modifier.height(25.dp))

            // Income, Balance and Expenditure
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryItem("Income", incomeStr)
                SummaryItem("Balance", balanceStr)
                SummaryItem("Expenditure", expenditureStr)
            }

            Spacer(modifier = Modifier.height(38.dp))

            // AI Summary card (opens Insights)
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(151.dp)
                    .clickable { onNavigateToInsights() }
                    .border(
                        width = 1.dp,
                        color = Color.LightGray.copy(alpha = 0.6f),
                        shape = RoundedCornerShape(7.dp)
                    )
                    .padding(20.dp),
                verticalAlignment = Alignment.Top
            ) {

                Text(
                    text = "ⓘ",
                    fontSize = 27.sp,
                    color = black
                )

                Spacer(modifier = Modifier.width(20.dp))

                Column {
                    Text(
                        text = "AI Summary",
                        fontSize = 20.sp,
                        fontWeight = FontWeight.Bold,
                        color = black
                    )

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "Your financial summary will appear here.",
                        fontSize = 14.sp,
                        color = grey
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Month so far chart card
            MonthSoFarChartCard(expenditureStr, transactions)

            Spacer(modifier = Modifier.height(32.dp))

            // Income and Expenditure buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                HomeButton("Income", onNavigateToIncome)
                HomeButton("Expenditure", onNavigateToExpenses)
            }
        }
    }
}

// Month so far chart card
@Composable
fun MonthSoFarChartCard(total: String, transactions: List<Transaction>) {
    val black = Color(0xFF222222)
    val grey = Color(0xFF888888)

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .border(
                width = 1.dp,
                color = Color.LightGray.copy(alpha = 0.6f),
                shape = RoundedCornerShape(7.dp)
            )
            .padding(20.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = "Month so far",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = black
            )
            Text(
                text = total,
                fontSize = 16.sp,
                fontWeight = FontWeight.SemiBold,
                color = black
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Compute dynamic data points based on expense transactions
        val expenseTxs = transactions.filter { it.isExpense }
        val maxAmount = expenseTxs.maxOfOrNull { abs(it.amount) }?.coerceAtLeast(100.0) ?: 100.0
        val dataPoints = listOf(1, 5, 10, 15, 20, 25, 30).map { day ->
            val match = expenseTxs.filter { it.day in (day..day + 4) }.sumOf { abs(it.amount) }
            (match / maxAmount).toFloat().coerceIn(0.1f, 1f)
        }
        val days = listOf("1", "5", "10", "15", "20", "25", "30")

        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(90.dp)
        ) {
            Canvas(modifier = Modifier.fillMaxSize()) {
                val barWidth = size.width / (dataPoints.size * 2 - 1)
                dataPoints.forEachIndexed { index, value ->
                    val left = index * (barWidth * 2)
                    val top = size.height * (1f - value)
                    drawRect(
                        color = Color(0xFF292929),
                        topLeft = Offset(left, top),
                        size = Size(barWidth, size.height - top)
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(8.dp))

        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            days.forEach { day ->
                Text(
                    text = day,
                    fontSize = 11.sp,
                    color = grey
                )
            }
        }
    }
}

// Summary item
@Composable
fun SummaryItem(title: String, amount: String) {
    Column {
        Text(
            text = title,
            fontSize = 12.sp,
            color = Color.Gray
        )

        Text(
            text = amount,
            fontSize = 14.sp,
            fontWeight = FontWeight.SemiBold,
            color = Color.DarkGray
        )
    }
}

// Home buttons
@Composable
fun HomeButton(text: String, onClick: () -> Unit = {}) {
    Button(
        onClick = onClick,
        colors = ButtonDefaults.buttonColors(
            containerColor = Color(0xFF292929)
        ),
        shape = RoundedCornerShape(7.dp),
        contentPadding = PaddingValues(
            horizontal = 14.dp,
            vertical = 5.dp
        )
    ) {
        Text(
            text = text,
            fontSize = 13.sp,
            fontWeight = FontWeight.Normal,
            color = Color.White
        )
    }
}
