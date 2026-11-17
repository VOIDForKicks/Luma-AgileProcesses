package com.example.luma_project.data

import java.sql.Date

data class Recurring(
    val recurringID: String,
    var isRecurring: Boolean,
    val startDate: Date,
    val endDate: Date,
    val dueDate: Date,
    var frequency: String,
)
