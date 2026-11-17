package com.example.luma_project.data

data class Transaction(
    val transactionID: String,
    val userID: String,
    var amount: Double,
    var catagory: String,
    var isExpense: Boolean,
    val accountID: String,
    val recurringID: String,
)
