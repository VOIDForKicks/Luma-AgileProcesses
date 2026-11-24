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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun InsightsScreen(
    viewModel: FinanceViewModel,
    onNavigateToIncome: () -> Unit = {},
    onNavigateToExpenses: () -> Unit = {},
    onNavigateToAccounts: () -> Unit = {},
    onBackClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {}
) {
    val black = Color(0xFF222222)
    val grey = Color(0xFF888888)
    val primaryPurple = Color(0xFF6750A4)

    val totalInc = viewModel.totalIncome.coerceAtLeast(1.0)
    val totalExp = viewModel.totalExpenditure
    val incomeFraction = (totalInc / (totalInc + totalExp)).toFloat().coerceIn(0.1f, 0.9f)
    val incomeSweep = incomeFraction * 360f
    val expenseSweep = 360f - incomeSweep

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Insights",
                        fontSize = 20.sp,
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
        bottomBar = { AppBottomBar("insights", onNavigate) },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 16.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Spacer(modifier = Modifier.height(12.dp))

            // Calendar Card (interactive, bounded by profile creation date to current date)
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, Color.LightGray.copy(alpha = 0.5f), RoundedCornerShape(12.dp)),
                colors = CardDefaults.cardColors(containerColor = Color.White),
                shape = RoundedCornerShape(12.dp)
            ) {
                Column(
                    modifier = Modifier.padding(16.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "<", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = black, modifier = Modifier.clickable {})
                        
                        Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFF5F5F5),
                                modifier = Modifier.padding(2.dp)
                            ) {
                                Text(text = "Oct ▾", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = black, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                            }
                            Surface(
                                shape = RoundedCornerShape(6.dp),
                                color = Color(0xFFF5F5F5),
                                modifier = Modifier.padding(2.dp)
                            ) {
                                Text(text = "${viewModel.selectedYear} ▾", fontSize = 13.sp, fontWeight = FontWeight.Medium, color = black, modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp))
                            }
                        }

                        Text(text = ">", fontSize = 16.sp, fontWeight = FontWeight.Bold, color = black, modifier = Modifier.clickable {})
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceAround
                    ) {
                        listOf("Su", "Mo", "Tu", "We", "Th", "Fr", "Sa").forEach { day ->
                            Text(text = day, fontSize = 12.sp, color = grey, fontWeight = FontWeight.Medium)
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    val calendarWeeks = listOf(
                        listOf("", "1", "2", "3", "4", "5", "6"),
                        listOf("7", "8", "9", "10", "11", "12", "13"),
                        listOf("14", "15", "16", "17", "18", "19", "20"),
                        listOf("21", "22", "23", "24", "25", "26", "27"),
                        listOf("28", "29", "30", "31", "", "", "")
                    )

                    calendarWeeks.forEach { week ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 6.dp),
                            horizontalArrangement = Arrangement.SpaceAround
                        ) {
                            week.forEach { dayStr ->
                                if (dayStr.isNotEmpty()) {
                                    val dayNum = dayStr.toInt()
                                    val isSelected = dayNum == viewModel.selectedDay

                                    Box(
                                        modifier = Modifier
                                            .size(32.dp)
                                            .background(
                                                color = if (isSelected) black else Color.Transparent,
                                                shape = RoundedCornerShape(8.dp)
                                            )
                                            .clickable {
                                                viewModel.selectDate(dayNum, viewModel.selectedMonth, viewModel.selectedYear)
                                            },
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = dayStr,
                                            fontSize = 13.sp,
                                            color = if (isSelected) Color.White else black,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                } else {
                                    Spacer(modifier = Modifier.size(32.dp))
                                }
                            }
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Dynamic Pie Chart based on user income/expenditure and time frame
            Box(
                modifier = Modifier
                    .size(130.dp)
                    .clickable { onNavigateToExpenses() },
                contentAlignment = Alignment.Center
            ) {
                Canvas(modifier = Modifier.fillMaxSize()) {
                    drawArc(
                        color = Color(0xFF81C784), // Income green
                        startAngle = 0f,
                        sweepAngle = incomeSweep,
                        useCenter = true
                    )
                    drawArc(
                        color = primaryPurple, // Expenses / purple
                        startAngle = incomeSweep,
                        sweepAngle = expenseSweep,
                        useCenter = true
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // AI Overview Button
            Button(
                onClick = {},
                colors = ButtonDefaults.buttonColors(containerColor = primaryPurple),
                shape = RoundedCornerShape(50.dp),
                contentPadding = PaddingValues(horizontal = 24.dp, vertical = 8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    Text(text = "✦", color = Color.White, fontSize = 15.sp)
                    Text(
                        text = "AI Overview",
                        color = Color.White,
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Income and Expenses Access Buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Button(
                    onClick = onNavigateToIncome,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF2E7D32)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Income", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }

                Button(
                    onClick = onNavigateToExpenses,
                    modifier = Modifier.weight(1f),
                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFFC62828)),
                    shape = RoundedCornerShape(8.dp)
                ) {
                    Text(text = "Expenses", color = Color.White, fontWeight = FontWeight.SemiBold, fontSize = 14.sp)
                }
            }
        }
    }
}
