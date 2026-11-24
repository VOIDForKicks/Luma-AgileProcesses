package com.example.luma_project.data

data class AccountMainType(
    val title: String,
    val description: String,
    val subTypes: List<String>
)

object AccountTypeOptions {
    val dailySpending = AccountMainType(
        title = "DAILY SPENDING",
        description = "A regular account you will spend money from.",
        subTypes = listOf("Checking", "Cash", "Credit Card", "Debit Card", "Other")
    )

    val savings = AccountMainType(
        title = "SAVINGS",
        description = "An account for saving money and creating goals.",
        subTypes = listOf("Emergency Fund", "Vacation", "New Home", "Wedding", "Retirement", "Investment", "Other")
    )

    val debt = AccountMainType(
        title = "DEBT",
        description = "An account used for managing debt.",
        subTypes = listOf("Credit Card", "Home Loan", "Student Loan", "Car Loan", "Personal Loan", "Medical Debt", "Mortgage", "Other")
    )

    val allMainTypes = listOf(dailySpending, savings, debt)
}
