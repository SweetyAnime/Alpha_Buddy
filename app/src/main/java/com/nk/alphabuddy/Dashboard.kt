package com.nk.alphabuddy

import android.app.AlertDialog
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import java.text.SimpleDateFormat
import java.util.*

class Dashboard : AppCompatActivity() {

    private lateinit var userPreferences: UserPreferences
    private lateinit var lottieTimeTable: LottieAnimationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        userPreferences = UserPreferences(this)
        lottieTimeTable = findViewById(R.id.timetableButton)

        supportActionBar?.hide()

        // Load Lottie animation
        lottieTimeTable.setAnimation("timetable.json") // Load from assets
        lottieTimeTable.playAnimation()
        lottieTimeTable.setOnClickListener { showTimeTableDialog() }
    }

    private val timetable = mapOf(
        "CSE_Semester 4" to mapOf(
            "Sunday" to listOf("09:00 AM - Math", "10:00 AM - Physics", "11:00 AM - Biology", "12:00 PM - Programming"),
            "Monday" to listOf("09:00 AM - Data Structures", "10:00 AM - Algorithms", "11:00 AM - Break", "12:00 PM - OOP")
        ),
        "AI_Semester 4" to mapOf(
            "Sunday" to listOf("09:00 AM - Math", "10:00 AM - Physics", "11:00 AM - Biology", "12:00 PM - Programming", "01:00 PM - Big Data", "02:00 PM - Operating Systems"),
            "Tuesday" to listOf("09:00 AM - English", "10:00 AM - Chemistry", "11:00 AM - Break", "12:00 PM - Programming", "01:00 PM - Big Data", "02:00 PM - Operating Systems")
        )
    )

    private fun showTimeTableDialog() {
        val department = userPreferences.getDepartment()
        val semester = userPreferences.getSemester()
        val key = "${department}_$semester"
        val dayOfWeek = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())

        val todaySchedule = timetable[key]?.get(dayOfWeek) ?: listOf("No classes today!")

        AlertDialog.Builder(this)
            .setTitle("Today's Timetable ($dayOfWeek)")
            .setMessage(todaySchedule.joinToString("\n"))
            .setPositiveButton("OK") { dialog, _ -> dialog.dismiss() }
            .show()
    }
}
