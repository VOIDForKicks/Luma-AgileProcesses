package com.example.luma_project

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionInfoScreen(
    transaction: TransactionItemModel = TransactionItemModel("Supermarket", "Groceries", "€64.50", "Oct 24", "Personal Account", "None"),
    onBackClick: () -> Unit = {}
) {
    var selectedType by remember { mutableStateOf("Expense") }
    var valueText by remember { mutableStateOf(transaction.amount) }
    var notesText by remember { mutableStateOf("Transaction notes for ${transaction.header}") }

    val black = Color(0xFF1D1B20)
    val grey = Color(0xFF79747E)
    val primaryPurple = Color(0xFF6750A4)
    val lightPurple = Color(0xFFEADDFF)

    Scaffold(
        topBar = {
            TopAppBar(
                title = { },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Text(
                            text = "←",
                            fontSize = 24.sp,
                            color = black,
                            fontWeight = FontWeight.Bold
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color.White
                )
            )
        },
        bottomBar = {
            NavigationBar(
                containerColor = Color.White
            ) {
                NavigationBarItem(
                    selected = true,
                    onClick = {},
                    icon = { Text("⌂", fontSize = 23.sp) },
                    label = { Text("Home") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { Text("⌕", fontSize = 25.sp) },
                    label = { Text("Search") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { Text("⇄", fontSize = 24.sp) },
                    label = { Text("Activity") }
                )
                NavigationBarItem(
                    selected = false,
                    onClick = {},
                    icon = { Text("▱", fontSize = 24.sp) },
                    label = { Text("Cards") }
                )
            }
        },
        containerColor = Color.White
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .background(Color.White)
                .padding(innerPadding)
                .padding(horizontal = 24.dp)
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Type selector pills: Expense, Income, Transfer
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                listOf("Expense", "Income", "Transfer").forEach { type ->
                    val isSelected = selectedType == type
                    Button(
                        onClick = { selectedType = type },
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) primaryPurple else lightPurple.copy(alpha = 0.5f)
                        ),
                        shape = RoundedCornerShape(20.dp),
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        modifier = Modifier.weight(1f)
                    ) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(4.dp)
                        ) {
                            Text(text = "✦", fontSize = 12.sp, color = if (isSelected) Color.White else primaryPurple)
                            Text(
                                text = type,
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Medium,
                                color = if (isSelected) Color.White else primaryPurple
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(20.dp))

            // Value Input Field
            OutlinedTextField(
                value = valueText,
                onValueChange = { valueText = it },
                label = { Text("Value", color = grey) },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryPurple,
                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.6f),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(20.dp))

            // Info rows: Category, Date, Account, Repeating
            InfoRowItem("Category", transaction.subhead)
            InfoRowItem("Date", transaction.date)
            InfoRowItem("Account", transaction.account)
            InfoRowItem("Repeating", transaction.repeating)

            Spacer(modifier = Modifier.height(16.dp))

            // Notes Section
            Text(
                text = "Notes:",
                fontSize = 14.sp,
                color = grey,
                fontWeight = FontWeight.Medium
            )

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedTextField(
                value = notesText,
                onValueChange = { notesText = it },
                shape = RoundedCornerShape(12.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = primaryPurple,
                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.6f),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(130.dp)
            )
        }
    }
}

@Composable
fun InfoRowItem(label: String, value: String) {
    val black = Color(0xFF1D1B20)
    val grey = Color(0xFF79747E)
    val primaryPurple = Color(0xFF6750A4)

    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 12.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Box(
                modifier = Modifier
                    .size(28.dp)
                    .background(Color(0xFFEADDFF), RoundedCornerShape(6.dp)),
                contentAlignment = Alignment.Center
            ) {
                Text(text = "✦", fontSize = 12.sp, color = primaryPurple)
            }
            Text(
                text = label,
                fontSize = 15.sp,
                color = black,
                fontWeight = FontWeight.Medium
            )
        }

        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = value,
                fontSize = 14.sp,
                color = grey
            )
            Text(
                text = "›",
                fontSize = 18.sp,
                color = grey
            )
        }
    }
}
