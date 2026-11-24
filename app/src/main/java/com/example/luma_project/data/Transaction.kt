package com.example.luma_project.data

data class Transaction(
    val transactionID: String = "",
    val userID: String = "",
    var title: String = "",
    var amount: Double = 0.0,
    var catagory: String = "",
    var isExpense: Boolean = false,
    val accountID: String = "",
    val recurringID: String = "",
    var day: Int = 1,
    var month: String = "Oct",
    var recurring: Recurring? = null,
) {
    val isIncome: Boolean
        get() = !isExpense

    val category: String
        get() = catagory
}
