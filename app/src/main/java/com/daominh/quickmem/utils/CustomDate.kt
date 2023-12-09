package com.daominh.quickmem.utils

import android.util.Log
import java.text.SimpleDateFormat
import java.time.LocalDate
import java.time.format.DateTimeFormatter
import java.util.Date
import java.util.Calendar

class CustomDate {
    private val formatter = SimpleDateFormat("dd/MM/yyyy")

    fun getCurrentDate(): String {
        return if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            LocalDate.now().format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))
        } else {
            formatter.format(Date())
        }
    }

    fun isDateGreaterThanCurrentDate(dateStr: String): Boolean {
        return try {
            val date = formatter.parse(dateStr)
            date != null && date.after(Date())
        } catch (e: Exception) {
            Log.e("SignupActivity", "Error parsing date. Ensure the date is in the format dd/MM/yyyy.", e)
            false
        }
    }

    fun isAgeGreaterThan22(dateStr: String): Boolean {
        if (dateStr.trim().isEmpty()) return false

        return try {
            val date = formatter.parse(dateStr)
            val eighteenYearsAgo = Calendar.getInstance().apply {
                time = Date()
                add(Calendar.YEAR, -22)
            }.time
            date != null && date.before(eighteenYearsAgo)
        } catch (e: Exception) {
            Log.e("SignUpActivity", "Error parsing date. Ensure the date is in the format dd/MM/yyyy.", e)
            false
        }
    }
}