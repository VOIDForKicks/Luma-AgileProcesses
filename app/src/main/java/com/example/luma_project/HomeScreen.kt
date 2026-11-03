package com.example.luma_project

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
fun HomeScreen() {

    val black = Color(0xFF222222)
    val grey = Color(0xFF888888)

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
                actions = {
                    Text(
                        text = "",
                        fontSize = 20.sp,
                        modifier = Modifier.padding(end = 16.dp)
                    )
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
                    icon = { Text("◆", fontSize = 23.sp) },
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
                .padding(horizontal = 19.dp)
        ) {

            Spacer(modifier = Modifier.height(25.dp))

            // Income, Balance and Expenditure
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                SummaryItem("Income", "€0.00")
                SummaryItem("Balance", "€0.00")
                SummaryItem("Expenditure", "€0.00")
            }

            Spacer(modifier = Modifier.height(38.dp))

            // AI Summary card
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(151.dp)
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

            Spacer(modifier = Modifier.height(88.dp))

            // Income and Expenditure buttons
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceEvenly
            ) {
                HomeButton("Income")
                HomeButton("Expenditure")
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
fun HomeButton(text: String) {

    Button(
        onClick = {},
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
