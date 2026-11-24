package com.example.luma_project

import com.example.luma_project.data.Account
import com.example.luma_project.data.AccountTypeOptions
import com.example.luma_project.data.Recurring
import com.example.luma_project.data.Transaction
import com.example.luma_project.data.User
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Test
import java.sql.Date

class DataModelTest {

    @Test
    fun testTopDownHierarchy() {
        val recurring = Recurring(
            recurringID = "rec_01",
            isRecurring = true,
            startDate = Date.valueOf("2025-01-01"),
            endDate = Date.valueOf("2025-12-31"),
            dueDate = Date.valueOf("2025-02-01"),
            frequency = "Monthly"
        )

        val transaction = Transaction(
            transactionID = "tx_01",
            userID = "user_01",
            amount = 100.0,
            catagory = "Utilities",
            isExpense = true,
            accountID = "acc_01",
            recurringID = "rec_01",
            recurring = recurring
        )

        val account = Account(
            AccountID = "acc_01",
            UserID = "user_01",
            aType = "Checking",
            aName = "Main Account",
            Balance = 1500.0,
            Income = 2000.0,
            Expenditure = 500.0,
            transactions = listOf(transaction)
        )

        val user = User(
            UserID = "user_01",
            uName = "johndoe",
            password = "password123",
            email = "john@example.com",
            phoneNumber = "1234567890",
            accounts = listOf(account)
        )

        assertEquals("user_01", user.UserID)
        assertEquals(1, user.accounts.size)
        val firstAccount = user.accounts.first()
        assertEquals("acc_01", firstAccount.AccountID)
        assertEquals(1, firstAccount.transactions.size)
        val firstTransaction = firstAccount.transactions.first()
        assertEquals("tx_01", firstTransaction.transactionID)
        assertNotNull(firstTransaction.recurring)
        assertEquals("Monthly", firstTransaction.recurring?.frequency)
    }

    @Test
    fun testAccountTypeOptions() {
        assertEquals(3, AccountTypeOptions.allMainTypes.size)
        assertEquals("DAILY SPENDING", AccountTypeOptions.dailySpending.title)
        assertTrue(AccountTypeOptions.dailySpending.subTypes.contains("Checking"))

        assertEquals("SAVINGS", AccountTypeOptions.savings.title)
        assertTrue(AccountTypeOptions.savings.subTypes.contains("Emergency Fund"))

        assertEquals("DEBT", AccountTypeOptions.debt.title)
        assertTrue(AccountTypeOptions.debt.subTypes.contains("Mortgage"))
    }
}
