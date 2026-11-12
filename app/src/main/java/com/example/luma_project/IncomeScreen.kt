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

data class IncomeModel(
    val title: String,
    val category: String,
    val amount: String,
    val date: String
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun IncomeScreen(onBackClick: () -> Unit = {}) {
    val black = Color(0xFF222222)
    val grey = Color(0xFF888888)

    val incomes = listOf(
        IncomeModel("Monthly Salary", "Employment", "€3,450.00", "Oct 01"),
        IncomeModel("Freelance Project", "Side Hustle", "€650.00", "Oct 12"),
        IncomeModel("Dividend Payout", "Investments", "€75.50", "Oct 19")
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Income Transactions",
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
                text = "Total Income This Month",
                fontSize = 14.sp,
                color = grey
            )
            Text(
                text = "€4,175.50",
                fontSize = 28.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF2E7D32)
            )

            Spacer(modifier = Modifier.height(24.dp))

            Text(
                text = "Income Sources",
                fontSize = 16.sp,
                fontWeight = FontWeight.Bold,
                color = black
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(incomes) { income ->
                    IncomeRowItem(income)
                }
            }
        }
    }
}

@Composable
fun IncomeRowItem(income: IncomeModel) {
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
                text = income.title,
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = black
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = income.category,
                fontSize = 12.sp,
                color = grey
            )
        }

        Column(
            horizontalAlignment = Alignment.End
        ) {
            Text(
                text = "+${income.amount}",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = Color(0xFF2E7D32)
            )
            Spacer(modifier = Modifier.height(2.dp))
            Text(
                text = income.date,
                fontSize = 12.sp,
                color = grey
            )
        }
    }
}
