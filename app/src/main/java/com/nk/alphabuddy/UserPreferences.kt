package com.nk.alphabuddy

import android.content.Context
import android.content.SharedPreferences

class UserPreferences(context: Context) {

    private val sharedPreferences: SharedPreferences =
        context.getSharedPreferences("AlphaBuddyPrefs", Context.MODE_PRIVATE)

    fun saveUserChoice(department: String, semester: String) {
        sharedPreferences.edit()
            .putString("department", department)
            .putString("semester", semester)
            .apply()
    }

    fun getDepartment(): String? {
        return sharedPreferences.getString("department", null)
    }

    fun getSemester(): String? {
        return sharedPreferences.getString("semester", null)
    }

    fun isUserChoiceSet(): Boolean {
        return getDepartment() != null && getSemester() != null
    }
}
