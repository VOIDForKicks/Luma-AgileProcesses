package com.example.luma_project.data

data class Account(
    val AccountID: String = "",
    val UserID: String = "",
    var aType: String = "",
    var aName: String = "",
    var Balance: Double = 0.0,
    var Income: Double = 0.0,
    var Expenditure: Double = 0.0,
    var jointUserEmail: String = "",
    var transactions: List<Transaction> = emptyList(),
)
