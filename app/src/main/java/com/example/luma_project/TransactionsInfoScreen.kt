package com.example.luma_project

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowCircleLeft
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp

private val ChipSelected = Color(0xFF625B71)
private val ChipUnselected = Color(0xFFE8DEF8)
private val ChipUnselectedText = Color(0xFF4A4458)
private val FieldBorder = Color(0xFFCAC4D0)
private val RowIconColor = Color(0xFF49454F)

@Composable
fun TransactionsInfoScreen(
    initialType: String = "Expense",
    onBackClick: () -> Unit = {},
    onSaveClick: (String, String, String) -> Unit = { _, _, _ -> }
) {
    var type by remember(initialType) {
        mutableStateOf(initialType)
    }

    var amount by remember { mutableStateOf("") }
    var notes by remember { mutableStateOf("") }


    Scaffold(
        containerColor = Color.White,
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 16.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally,
        ) {
            // Back button
            Row(modifier = Modifier.fillMaxWidth()) {
                IconButton(onClick = onBackClick) {
                    Icon(
                        imageVector = Icons.Outlined.ArrowCircleLeft,
                        contentDescription = "Back",
                        modifier = Modifier.size(36.dp),
                    )
                }
            }

            // Expense / Income / Transfer selector
            Row(
                horizontalArrangement = Arrangement.spacedBy(2.dp),
                modifier = Modifier.padding(top = 4.dp),
            ) {
                listOf("Expense", "Income", "Transfer").forEach { label ->
                    TypeChip(
                        label = label,
                        selected = type == label,
                        onClick = { type = label },
                    )
                }
            }

            Spacer(Modifier.height(24.dp))

            // Value field
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                placeholder = { Text("Value") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ChipSelected,
                    unfocusedBorderColor = FieldBorder,
                ),
                modifier = Modifier.fillMaxWidth(0.8f),
            )

            Spacer(Modifier.height(24.dp))

            // Option rows
            Column(
                modifier = Modifier.fillMaxWidth(0.8f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OptionRow("Category") { /* TODO open category picker */ }
                OptionRow("Date") { /* TODO open date picker */ }
                OptionRow("Account") { /* TODO open account picker */ }
                OptionRow("Repeating") { /* TODO open repeat options */ }
            }

            Spacer(Modifier.height(16.dp))

            // Notes
            Column(modifier = Modifier.fillMaxWidth(0.85f)) {
                Text(text = "Notes:", fontSize = 14.sp, modifier = Modifier.padding(start = 4.dp, bottom = 2.dp))
                OutlinedTextField(
                    value = notes,
                    onValueChange = { notes = it },
                    shape = RoundedCornerShape(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = ChipSelected,
                        unfocusedBorderColor = FieldBorder,
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(190.dp),
                )
            }

            Spacer(Modifier.height(24.dp))
        }
    }
}

@Composable
private fun TypeChip(label: String, selected: Boolean, onClick: () -> Unit) {
    Surface(
        onClick = onClick,
        shape = RoundedCornerShape(50),
        color = if (selected) ChipSelected else ChipUnselected,
    ) {
        Row(
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 8.dp),
            verticalAlignment = Alignment.CenterVertically,
        ) {
            Icon(
                imageVector = Icons.Outlined.StarBorder,
                contentDescription = null,
                tint = if (selected) Color.White else ChipUnselectedText,
                modifier = Modifier.size(18.dp),
            )
            Spacer(Modifier.width(4.dp))
            Text(
                text = label,
                fontSize = 14.sp,
                fontWeight = FontWeight.Medium,
                color = if (selected) Color.White else ChipUnselectedText,
            )
        }
    }
}

@Composable
private fun OptionRow(
    label: String,
    value: String = "Select",
    onClick: () -> Unit,
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(vertical = 12.dp),
        verticalAlignment = Alignment.CenterVertically,
    ) {
        Icon(
            imageVector = Icons.Outlined.StarBorder,
            contentDescription = null,
            tint = RowIconColor,
            modifier = Modifier.size(22.dp),
        )
        Text(
            text = label,
            fontSize = 16.sp,
            modifier = Modifier
                .weight(1f)
                .padding(start = 16.dp),
        )
        Text(text = value, fontSize = 13.sp, color = RowIconColor)
        Text(text = "  ▸", fontSize = 12.sp, color = RowIconColor)
    }
}

@Preview(showBackground = true)
@Composable
private fun TransactionsInfoScreenPreview() {
    MaterialTheme { TransactionsInfoScreen() }
}