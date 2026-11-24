package com.example.luma_project.data

import java.sql.Date

data class Recurring(
    val recurringID: String = "",
    var isRecurring: Boolean = false,
    val startDate: Date? = null,
    val endDate: Date? = null,
    val dueDate: Date? = null,
    var frequency: String = "",
)
