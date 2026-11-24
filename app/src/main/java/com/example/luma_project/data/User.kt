package com.example.luma_project.data

data class User(
    val UserID: String = "",
    val uName: String = "",
    var password: String = "",
    val email: String = "",
    val phoneNumber: String = "",
    var accounts: List<Account> = emptyList(),
)
