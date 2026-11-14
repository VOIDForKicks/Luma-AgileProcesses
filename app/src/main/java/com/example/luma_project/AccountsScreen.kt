package com.example.luma_project

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowCircleLeft
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

// ---------- Model ----------

data class Account(val name: String, val balance: String)

// ---------- Colors (taken from the design) ----------

private val CardBackground = Color(0xFFFDF7FF)
private val CardBorder = Color(0xFFCAC4D0)
private val AvatarBackground = Color(0xFFE8DEF8)
private val AvatarText = Color(0xFF4F378B)
private val TextPrimary = Color(0xFF1D1B20)

// ---------- Screen ----------

@Composable
fun AccountsScreen(
    netWorth: String,
    accounts: List<Account>,
    onBackClick: () -> Unit = {},
    onNavigate: (String) -> Unit = {},
) {
    Scaffold(
        containerColor = Color.White,
        bottomBar = { AppBottomBar("accounts", onNavigate) },
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            Header(netWorth = netWorth, onBackClick = onBackClick)

            LazyColumn(
                modifier = Modifier.fillMaxSize(),
                contentPadding = PaddingValues(horizontal = 8.dp, vertical = 8.dp),
                verticalArrangement = Arrangement.spacedBy(16.dp),
            ) {
                items(accounts) { account ->
                    AccountCard(account)
                }
            }
        }
    }
}

@Composable
private fun Header(netWorth: String, onBackClick: () -> Unit) {
    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 8.dp, vertical = 8.dp)
    ) {
        IconButton(
            onClick = onBackClick,
            modifier = Modifier.align(Alignment.TopStart),
        ) {
            Icon(
                imageVector = Icons.Outlined.ArrowCircleLeft,
                contentDescription = "Back",
                modifier = Modifier.size(36.dp),
            )
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 8.dp),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            Icon(
                imageVector = Icons.Outlined.StarBorder,
                contentDescription = null,
                modifier = Modifier.size(48.dp),
            )
            Text(
                text = "Accounts",
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 12.dp),
            )
            Text(
                text = netWorth,
                fontSize = 22.sp,
                fontWeight = FontWeight.SemiBold,
                color = TextPrimary,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(top = 16.dp, bottom = 8.dp),
            )
        }
    }
}

@Composable
private fun AccountCard(account: Account) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(12.dp),
        colors = CardDefaults.cardColors(containerColor = CardBackground),
        border = BorderStroke(1.dp, CardBorder),
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            // Avatar with first letter
            Box(
                modifier = Modifier
                    .size(40.dp)
                    .background(AvatarBackground, CircleShape),
                contentAlignment = Alignment.Center,
            ) {
                Text(
                    text = account.name.firstOrNull()?.uppercase() ?: "",
                    color = AvatarText,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                )
            }

            Column(modifier = Modifier.padding(start = 16.dp)) {
                Text(
                    text = account.name,
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = TextPrimary,
                )
                Text(
                    text = account.balance,
                    fontSize = 14.sp,
                    color = TextPrimary,
                    modifier = Modifier.padding(top = 4.dp),
                )
            }
        }
    }
}

// ---------- Preview ----------

@Preview(showBackground = true)
@Composable
private fun AccountsScreenPreview() {
    MaterialTheme {
        AccountsScreen(
            netWorth = "123,456",
            accounts = List(5) { Account("Account Name", "12,345") },
        )
    }
}
