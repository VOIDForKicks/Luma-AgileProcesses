package com.example.luma_project

import android.app.DatePickerDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ArrowCircleLeft
import androidx.compose.material.icons.outlined.StarBorder
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.luma_project.data.Account
import com.example.luma_project.data.AccountTypeOptions
import com.example.luma_project.data.Recurring
import java.sql.Date
import java.util.Calendar
import java.util.UUID

private val ChipSelected = Color(0xFF625B71)
private val ChipUnselected = Color(0xFFE8DEF8)
private val ChipUnselectedText = Color(0xFF4A4458)
private val FieldBorder = Color(0xFFCAC4D0)
private val RowIconColor = Color(0xFF49454F)

@Composable
fun TransactionsInfoScreen(
    accounts: List<Account> = emptyList(),
    onSaveTransaction: (String, String, String, Double, Boolean, Int, String, Recurring?) -> Unit = { _, _, _, _, _, _, _, _ -> },
    onBackClick: () -> Unit = {},
    onSaveClick: (String, String, String) -> Unit = { _, _, _ -> }
) {
    var type by remember { mutableStateOf("Income") } // Income first by default
    var title by remember { mutableStateOf("") }
    var amount by remember { mutableStateOf("") }
    var category by remember { mutableStateOf("General") }

    val defaultAcc = accounts.firstOrNull()
    var selectedAccountId by remember { mutableStateOf(defaultAcc?.AccountID ?: "") }
    var selectedAccountName by remember { mutableStateOf(defaultAcc?.aName ?: "Select Account") }
    var selectedAccountType by remember { mutableStateOf(defaultAcc?.aType ?: "Checking") }

    var selectedDay by remember { mutableStateOf(Calendar.getInstance().get(Calendar.DAY_OF_MONTH)) }
    var selectedMonthStr by remember { mutableStateOf("Oct") }
    var dateStr by remember { mutableStateOf("Oct ${"%02d".format(selectedDay)}, 2025") }

    // Repeating options
    var isRepeating by remember { mutableStateOf(false) }
    var frequency by remember { mutableStateOf("Monthly") }

    var notes by remember { mutableStateOf("") }
    var showAccountPicker by remember { mutableStateOf(false) }
    var showCategoryPicker by remember { mutableStateOf(false) }
    var showFrequencyPicker by remember { mutableStateOf(false) }

    val context = LocalContext.current

    val months = listOf("Jan", "Feb", "Mar", "Apr", "May", "Jun", "Jul", "Aug", "Sep", "Oct", "Nov", "Dec")

    // Determine subtypes based on account type
    val categorySubtypes = remember(selectedAccountType) {
        when {
            selectedAccountType.contains("Saving", ignoreCase = true) -> AccountTypeOptions.savings.subTypes
            selectedAccountType.contains("Debt", ignoreCase = true) || selectedAccountType.contains("Loan", ignoreCase = true) || selectedAccountType.contains("Mortgage", ignoreCase = true) -> AccountTypeOptions.debt.subTypes
            else -> AccountTypeOptions.dailySpending.subTypes
        }
    }


    Scaffold(
        containerColor = Color.White,
        bottomBar = {
            Button(
                onClick = {
                    val amt = amount.toDoubleOrNull() ?: 0.0
                    val isExp = type == "Expense"
                    if (amt > 0.0 && selectedAccountId.isNotEmpty()) {
                        val recurringObj = if (isRepeating) {
                            Recurring(
                                recurringID = UUID.randomUUID().toString(),
                                isRecurring = true,
                                startDate = Date(System.currentTimeMillis()),
                                frequency = frequency
                            )
                        } else {
                            null
                        }

                        onSaveTransaction(
                            selectedAccountId,
                            if (title.isBlank()) category else title,
                            category,
                            amt,
                            isExp,
                            selectedDay,
                            selectedMonthStr,
                            recurringObj
                        )
                        onBackClick()
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                colors = ButtonDefaults.buttonColors(containerColor = ChipSelected)
            ) {
                Text("Save Transaction", color = Color.White, fontSize = 16.sp)
            }
        }
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

            // Income / Expense selector (Income first)
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.padding(top = 4.dp),
            ) {
                listOf("Income", "Expense").forEach { label ->
                    TypeChip(
                        label = label,
                        selected = type == label,
                        onClick = { type = label },
                    )
                }
            }

            Spacer(Modifier.height(20.dp))

            // Title field
            OutlinedTextField(
                value = title,
                onValueChange = { title = it },
                placeholder = { Text("Title (e.g. Salary)") },
                singleLine = true,
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ChipSelected,
                    unfocusedBorderColor = FieldBorder,
                ),
                modifier = Modifier.fillMaxWidth(0.85f),
            )

            Spacer(Modifier.height(16.dp))

            // Value field
            OutlinedTextField(
                value = amount,
                onValueChange = { amount = it },
                placeholder = { Text("Value (Amount)") },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal),
                shape = RoundedCornerShape(8.dp),
                colors = OutlinedTextFieldDefaults.colors(
                    focusedBorderColor = ChipSelected,
                    unfocusedBorderColor = FieldBorder,
                ),
                modifier = Modifier.fillMaxWidth(0.85f),
            )

            Spacer(Modifier.height(20.dp))

            // Option rows (Account above Category)
            Column(
                modifier = Modifier.fillMaxWidth(0.85f),
                verticalArrangement = Arrangement.spacedBy(8.dp),
            ) {
                OptionRow("Account", selectedAccountName) {
                    showAccountPicker = true
                }

                OptionRow("Category", category) {
                    showCategoryPicker = true
                }

                OptionRow("Date", dateStr) {
                    val cal = Calendar.getInstance()
                    DatePickerDialog(
                        context,
                        { _, year, month, dayOfMonth ->
                            selectedDay = dayOfMonth
                            selectedMonthStr = months[month]
                            dateStr = "$selectedMonthStr ${"%02d".format(dayOfMonth)}, $year"
                        },
                        cal.get(Calendar.YEAR),
                        cal.get(Calendar.MONTH),
                        cal.get(Calendar.DAY_OF_MONTH)
                    ).apply {
                        datePicker.minDate = Calendar.getInstance().apply { set(2025, 8, 1) }.timeInMillis
                        datePicker.maxDate = System.currentTimeMillis()
                    }.show()
                }

                OptionRow("Repeating", if (isRepeating) "Yes" else "No") {
                    isRepeating = !isRepeating
                }

                if (isRepeating) {
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp),
                        colors = CardDefaults.cardColors(containerColor = Color(0xFFF9F9F9)),
                        shape = RoundedCornerShape(8.dp)
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text("Repeating Details", fontWeight = FontWeight.Bold, fontSize = 14.sp)
                            Spacer(Modifier.height(8.dp))
                            OptionRow("Frequency", frequency) {
                                showFrequencyPicker = true
                            }
                        }
                    }
                }
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
                        .height(120.dp),
                )
            }

            Spacer(Modifier.height(24.dp))
        }

        // Account Picker Dialog
        if (showAccountPicker) {
            AlertDialog(
                onDismissRequest = { showAccountPicker = false },
                title = { Text("Select Account") },
                text = {
                    Column {
                        if (accounts.isEmpty()) {
                            Text("No accounts available. Please create an account first.")
                        } else {
                            accounts.forEach { acc ->
                                TextButton(
                                    onClick = {
                                        selectedAccountId = acc.AccountID
                                        selectedAccountName = acc.aName
                                        selectedAccountType = acc.aType
                                        showAccountPicker = false
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                ) {
                                    Text("${acc.aName} (${acc.aType})", fontSize = 16.sp)
                                }
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showAccountPicker = false }) { Text("Close") }
                }
            )
        }

        // Category Picker Dialog (using subtypes from account type)
        if (showCategoryPicker) {
            AlertDialog(
                onDismissRequest = { showCategoryPicker = false },
                title = { Text("Select Category ($selectedAccountType)") },
                text = {
                    Column(modifier = Modifier.verticalScroll(rememberScrollState())) {
                        categorySubtypes.forEach { subtype ->
                            TextButton(
                                onClick = {
                                    category = subtype
                                    showCategoryPicker = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(subtype, fontSize = 16.sp)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showCategoryPicker = false }) { Text("Close") }
                }
            )
        }

        // Frequency Picker Dialog
        if (showFrequencyPicker) {
            AlertDialog(
                onDismissRequest = { showFrequencyPicker = false },
                title = { Text("Select Frequency") },
                text = {
                    Column {
                        listOf("Daily", "Weekly", "Monthly", "Yearly").forEach { freq ->
                            TextButton(
                                onClick = {
                                    frequency = freq
                                    showFrequencyPicker = false
                                },
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Text(freq, fontSize = 16.sp)
                            }
                        }
                    }
                },
                confirmButton = {
                    TextButton(onClick = { showFrequencyPicker = false }) { Text("Close") }
                }
            )
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
            modifier = Modifier.padding(horizontal = 14.dp, vertical = 8.dp),
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
