package com.nk.alphabuddy

import android.content.Context
import android.content.SharedPreferences
import androidx.core.content.edit

class UserPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AlphaBuddyPrefs", Context.MODE_PRIVATE)

    fun saveUserChoice(department: String, semester: String) {
        sharedPreferences.edit {
            putString("department", department)
                .putString("semester", semester)
        }
    }

    fun getDepartment(): String? {
        return sharedPreferences.getString("department", null)
    }

    fun getSemester(): String? {
        return sharedPreferences.getString("semester", null)
    }

}
