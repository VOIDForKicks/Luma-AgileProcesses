package com.example.luma_project

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

data class TransactionItemModel(
    val header: String,
    val subhead: String,
    val amount: String = "64.50",
    val date: String = "Oct 24, 2025",
    val account: String = "Main Account",
    val repeating: String = "None",
    val type: String = "Expense" // "Expense" or "Income"
)

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TransactionsScreen(
    onTabSelected: (String) -> Unit = {},
    onTransactionClick: (TransactionItemModel) -> Unit = {}
) {
    var selectedTab by remember { mutableStateOf("Expenses") }
    var searchQuery by remember { mutableStateOf("") }

    val black = Color(0xFF222222)
    val grey = Color(0xFF79747E)
    val primaryPurple = Color(0xFF6750A4)

    val allTransactions = listOf(
        TransactionItemModel("Supermarket", "Groceries", "€64.50", "Oct 24", "Personal Account", "Weekly", "Expense"),
        TransactionItemModel("Monthly Salary", "Employment", "€3,450.00", "Oct 01", "Main Account", "Monthly", "Income"),
        TransactionItemModel("Electric Bill", "Utilities", "€85.00", "Oct 22", "Main Account", "Monthly", "Expense"),
        TransactionItemModel("Freelance Project", "Side Hustle", "€650.00", "Oct 12", "Main Account", "None", "Income"),
        TransactionItemModel("Coffee Shop", "Food & Drink", "€4.20", "Oct 21", "Personal Account", "None", "Expense"),
        TransactionItemModel("Dividend Payout", "Investments", "€75.50", "Oct 19", "Main Account", "Monthly", "Income"),
        TransactionItemModel("Public Transport", "Travel", "€35.00", "Oct 20", "Main Account", "Weekly", "Expense")
    )

    val filteredTransactions = when (selectedTab) {
        "Expenses" -> allTransactions.filter { it.type == "Expense" && (searchQuery.isBlank() || it.header.contains(searchQuery, ignoreCase = true) || it.subhead.contains(searchQuery, ignoreCase = true)) }
        "Income" -> allTransactions.filter { it.type == "Income" && (searchQuery.isBlank() || it.header.contains(searchQuery, ignoreCase = true) || it.subhead.contains(searchQuery, ignoreCase = true)) }
        else -> allTransactions.filter { searchQuery.isBlank() || it.header.contains(searchQuery, ignoreCase = true) || it.subhead.contains(searchQuery, ignoreCase = true) }
    }

    Scaffold(
        topBar = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .background(Color.White)
            ) {
                // Title
                Text(
                    text = "Transactions",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Bold,
                    color = black,
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 12.dp)
                )

                // Tabs: Expenses, Income, AI Overview
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 16.dp),
                    horizontalArrangement = Arrangement.SpaceAround
                ) {
                    listOf("Expenses", "Income", "AI Overview").forEach { tab ->
                        val isSelected = selectedTab == tab
                        Column(
                            horizontalAlignment = Alignment.CenterHorizontally,
                            modifier = Modifier
                                .clickable {
                                    selectedTab = tab
                                    onTabSelected(tab)
                                }
                                .padding(vertical = 8.dp)
                        ) {
                            Text(
                                text = tab,
                                fontSize = 14.sp,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                                color = if (isSelected) primaryPurple else grey
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            if (isSelected) {
                                Box(
                                    modifier = Modifier
                                        .width(40.dp)
                                        .height(3.dp)
                                        .background(primaryPurple, RoundedCornerShape(1.5.dp))
                                )
                            } else {
                                Spacer(modifier = Modifier.height(3.dp))
                            }
                        }
                    }
                }
                HorizontalDivider(color = Color.LightGray.copy(alpha = 0.3f))
            }
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
                .padding(horizontal = 16.dp)
        ) {
            Spacer(modifier = Modifier.height(16.dp))

            // Search Bar ("Value" placeholder with search icon)
            OutlinedTextField(
                value = searchQuery,
                onValueChange = { searchQuery = it },
                placeholder = { Text("Value", color = grey) },
                trailingIcon = {
                    Text(text = "⌕", fontSize = 20.sp, color = grey)
                },
                shape = RoundedCornerShape(28.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = Color.LightGray,
                    unfocusedBorderColor = Color.LightGray.copy(alpha = 0.6f),
                    focusedContainerColor = Color.White,
                    unfocusedContainerColor = Color.White
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(52.dp),
                singleLine = true
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Transaction Cards List
            LazyColumn(
                verticalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxSize()
            ) {
                items(filteredTransactions) { tx ->
                    TransactionCardItem(tx, onClick = { onTransactionClick(tx) })
                }
            }
        }
    }
}

@Composable
fun TransactionCardItem(item: TransactionItemModel, onClick: () -> Unit) {
    val black = Color(0xFF1D1B20)
    val grey = Color(0xFF49454F)
    val cardBackground = Color(0xFFF7F2FA)
    val avatarBackground = Color(0xFFEADDFF)
    val avatarText = Color(0xFF21005D)

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick),
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        shape = RoundedCornerShape(16.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                // Circular Avatar with first letter of header
                Box(
                    modifier = Modifier
                        .size(40.dp)
                        .clip(CircleShape)
                        .background(avatarBackground),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = item.header.take(1),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold,
                        color = avatarText
                    )
                }

                // Header & Subhead
                Column {
                    Text(
                        text = item.header,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = black
                    )
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = item.subhead,
                        fontSize = 14.sp,
                        color = grey
                    )
                }
            }

            // Amount instead of 3 symbols
            Text(
                text = if (item.type == "Income") "+${item.amount}" else "-${item.amount}",
                fontSize = 15.sp,
                fontWeight = FontWeight.SemiBold,
                color = if (item.type == "Income") Color(0xFF2E7D32) else Color(0xFFC62828)
            )
        }
    }
}
