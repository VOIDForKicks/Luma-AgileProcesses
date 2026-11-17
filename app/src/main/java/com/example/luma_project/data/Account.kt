package com.example.luma_project.data

data class Account(
    val AccountID: String,
    val UserID: String,
    var aType: String,
    var aName: String,
    var Balance: Double,
    var Income: Double,
    var Expenditure: Double,
)
