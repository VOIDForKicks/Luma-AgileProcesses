package com.example.luma_project

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.example.luma_project.data.Account
import com.example.luma_project.data.Recurring
import com.example.luma_project.data.Transaction
import com.example.luma_project.data.User
import java.sql.Date
import java.util.Calendar
import java.util.UUID
import kotlin.math.abs

class FinanceViewModel : ViewModel() {
    val profileCreationYear = 2025
    val profileCreationMonth = 9 // September
    val profileCreationDay = 1

    var selectedYear by mutableStateOf(2025)
    var selectedMonth by mutableStateOf(10) // October
    var selectedDay by mutableStateOf(24)

    var user by mutableStateOf(
        User(
            UserID = "user_01",
            uName = "John Doe",
            password = "",
            email = "john@example.com",
            phoneNumber = "1234567890",
            accounts = emptyList()
        )
    )

    fun addAccount(name: String, type: String, initialBalance: Double, jointUserEmail: String = "") {
        val newAccount = Account(
            AccountID = UUID.randomUUID().toString(),
            UserID = user.UserID,
            aType = type,
            aName = name,
            Balance = initialBalance,
            Income = 0.0,
            Expenditure = 0.0,
            jointUserEmail = jointUserEmail,
            transactions = emptyList()
        )
        user = user.copy(accounts = user.accounts + newAccount)
    }

    fun addTransaction(
        accountID: String,
        title: String,
        category: String,
        amount: Double,
        isExpense: Boolean,
        day: Int,
        month: String = "Oct",
        recurring: Recurring? = null
    ) {
        val targetAccId = accountID.ifEmpty { user.accounts.firstOrNull()?.AccountID ?: "" }
        if (targetAccId.isEmpty()) return

        val finalAmount = if (isExpense) -abs(amount) else abs(amount)
        val absAmount = abs(amount)

        val newTx = Transaction(
            transactionID = UUID.randomUUID().toString(),
            userID = user.UserID,
            title = title,
            amount = finalAmount,
            catagory = category,
            isExpense = isExpense,
            accountID = targetAccId,
            recurringID = recurring?.recurringID ?: "",
            day = day,
            month = month,
            recurring = recurring
        )

        val updatedAccounts = user.accounts.map { acc ->
            if (acc.AccountID == targetAccId) {
                val newTransactions = acc.transactions + newTx
                val multiplier = if (recurring != null && recurring.isRecurring) {
                    calculateRecurringMultiplier(recurring.startDate, recurring.frequency)
                } else {
                    1
                }

                val effectiveAmount = finalAmount * multiplier
                val effectiveAbsAmount = absAmount * multiplier

                val newIncome = if (!isExpense) acc.Income + effectiveAbsAmount else acc.Income
                val newExpenditure = if (isExpense) acc.Expenditure + effectiveAbsAmount else acc.Expenditure
                val newBalance = acc.Balance + effectiveAmount

                acc.copy(
                    transactions = newTransactions,
                    Income = newIncome,
                    Expenditure = newExpenditure,
                    Balance = newBalance
                )
            } else {
                acc
            }
        }
        user = user.copy(accounts = updatedAccounts)
    }

    private fun calculateRecurringMultiplier(startDate: Date?, frequency: String, currentDate: Calendar = Calendar.getInstance()): Int {
        if (startDate == null) return 1
        val startCal = Calendar.getInstance().apply { time = startDate }
        val diffMillis = currentDate.timeInMillis - startCal.timeInMillis
        if (diffMillis <= 0) return 1

        val daysDiff = (diffMillis / (1000 * 60 * 60 * 24)).toInt()
        return when (frequency.lowercase()) {
            "daily" -> (daysDiff + 1).coerceAtLeast(1)
            "weekly" -> (daysDiff / 7 + 1).coerceAtLeast(1)
            "monthly" -> {
                val monthsDiff = (currentDate.get(Calendar.YEAR) - startCal.get(Calendar.YEAR)) * 12 +
                        (currentDate.get(Calendar.MONTH) - startCal.get(Calendar.MONTH))
                (monthsDiff + 1).coerceAtLeast(1)
            }
            "yearly" -> {
                val yearsDiff = currentDate.get(Calendar.YEAR) - startCal.get(Calendar.YEAR)
                (yearsDiff + 1).coerceAtLeast(1)
            }
            else -> 1
        }
    }

    fun selectDate(day: Int, month: Int, year: Int) {
        val calendarSelected = Calendar.getInstance().apply { set(year, month - 1, day) }
        val calendarMin = Calendar.getInstance().apply { set(profileCreationYear, profileCreationMonth - 1, profileCreationDay) }
        val calendarMax = Calendar.getInstance()

        if (!calendarSelected.before(calendarMin) && !calendarSelected.after(calendarMax)) {
            selectedDay = day
            selectedMonth = month
            selectedYear = year
        }
    }

    val totalIncome: Double
        get() = user.accounts.sumOf { acc ->
            acc.transactions.sumOf { tx ->
                val multiplier = if (tx.recurring != null && tx.recurring?.isRecurring == true) {
                    calculateRecurringMultiplier(tx.recurring?.startDate, tx.recurring?.frequency ?: "Monthly")
                } else {
                    1
                }
                if (!tx.isExpense) abs(tx.amount) * multiplier else 0.0
            }
        }

    val totalExpenditure: Double
        get() = user.accounts.sumOf { acc ->
            acc.transactions.sumOf { tx ->
                val multiplier = if (tx.recurring != null && tx.recurring?.isRecurring == true) {
                    calculateRecurringMultiplier(tx.recurring?.startDate, tx.recurring?.frequency ?: "Monthly")
                } else {
                    1
                }
                if (tx.isExpense) abs(tx.amount) * multiplier else 0.0
            }
        }

    val netWorth: Double
        get() = user.accounts.sumOf { acc ->
            acc.Balance + acc.transactions.sumOf { tx ->
                val multiplier = if (tx.recurring != null && tx.recurring?.isRecurring == true) {
                    calculateRecurringMultiplier(tx.recurring?.startDate, tx.recurring?.frequency ?: "Monthly")
                } else {
                    1
                }
                if (multiplier > 1) tx.amount * (multiplier - 1) else 0.0
            }
        }

    val allTransactions: List<Triple<Transaction, Account, Int>>
        get() = user.accounts.flatMap { acc ->
            acc.transactions.map { tx ->
                Triple(tx, acc, tx.day)
            }
        }

    val filteredTransactions: List<Triple<Transaction, Account, Int>>
        get() = allTransactions.filter { (_, _, day) ->
            selectedDay == 0 || day == selectedDay
        }
}
