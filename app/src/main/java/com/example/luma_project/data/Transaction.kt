package com.example.luma_project.data

data class Transaction(
    val title: String,
    val category: String,
    val amount: Double,
    val day: Int,
    val month: String = "Oct",
    val isIncome: Boolean,
)
