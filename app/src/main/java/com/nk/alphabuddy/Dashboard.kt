package com.nk.alphabuddy

import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import java.text.SimpleDateFormat
import java.util.*

class Dashboard : AppCompatActivity() {

    private lateinit var userPreferences: UserPreferences
    private lateinit var timetableOverlay: LinearLayout
    private lateinit var timetableText: TextView
    private lateinit var daySelector: Spinner

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        userPreferences = UserPreferences(this)
        supportActionBar?.hide()

        // Check if department and semester exist; if not, prompt user
        if (userPreferences.getDepartment() == null || userPreferences.getSemester() == null) {
            showDepartmentSelectionDialog()
        }

        val timetableButton = findViewById<LottieAnimationView>(R.id.timetableButton)
        timetableOverlay = findViewById(R.id.timetableOverlay)
        timetableText = findViewById(R.id.timetableText)
        daySelector = findViewById(R.id.daySelector)
        val closeOverlayButton = findViewById<Button>(R.id.closeOverlayButton)

        // Show overlay when button is clicked
        timetableButton.setOnClickListener {
            timetableOverlay.visibility = View.VISIBLE
        }

        // Hide overlay when close button is clicked
        closeOverlayButton.setOnClickListener {
            timetableOverlay.visibility = View.GONE
        }

        // Populate Spinner with days of the week
        val daysOfWeek = arrayOf("Sunday", "Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, daysOfWeek)
        daySelector.adapter = adapter

        // Set default selection to current day
        val currentDay = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
        val defaultPosition = daysOfWeek.indexOf(currentDay)
        if (defaultPosition != -1) {
            daySelector.setSelection(defaultPosition)
            updateTimetable(currentDay)
        }

        // Handle day selection change
        daySelector.onItemSelectedListener = object : AdapterView.OnItemSelectedListener {
            override fun onItemSelected(parent: AdapterView<*>?, view: View?, position: Int, id: Long) {
                val selectedDay = daysOfWeek[position]
                updateTimetable(selectedDay)
            }

            override fun onNothingSelected(parent: AdapterView<*>?) {}
        }
    }

    private fun showDepartmentSelectionDialog() {
        val departments = arrayOf("CSE", "AI", "ECE", "IT")
        val semesters = arrayOf("1", "2", "3", "4")

        val departmentSpinner = Spinner(this)
        departmentSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, departments)

        val semesterSpinner = Spinner(this)
        semesterSpinner.adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, semesters)

        val layout = LinearLayout(this).apply {
            orientation = LinearLayout.VERTICAL
            setPadding(32, 32, 32, 32)
            addView(departmentSpinner)
            addView(semesterSpinner)
        }

        AlertDialog.Builder(this)
            .setTitle("Select Department & Semester")
            .setView(layout)
            .setPositiveButton("Save") { _, _ ->
                val selectedDepartment = departments[departmentSpinner.selectedItemPosition]
                val selectedSemester = semesters[semesterSpinner.selectedItemPosition]

                userPreferences.saveUserChoice(selectedDepartment, selectedSemester)
                updateTimetable(SimpleDateFormat("EEEE", Locale.getDefault()).format(Date()))
            }
            .setCancelable(false)
            .show()
    }

    private val timetable = mapOf(
        "CSE_4" to mapOf("Sunday" to listOf("Holiday")),

        "AI_4" to mapOf(
            "Sunday" to listOf("Holiday"),
            "Monday" to listOf("08:30 AM - DSA/CN Lab", "09:30 AM - DSA/CN Lab", "10:20 AM - Break", "10:30 AM - Fundamentals of Data Science & Analysis", "11:20 AM - Probability & Statistics", "12:10 PM - Operating Systems","01:00 PM - Lunch","01:30 PM - Environmental Science & Sustainability","02:15 PM - Machine Language","03:00 PM - Computer Networks"),
            "Tuesday" to listOf("08:30 AM - Machine Language", "09:30 AM - Probability & Statistics", "10:20 AM - Break", "10:30 AM - Computer Networks", "11:20 AM - Environmental Science & Sustainability", "12:10 PM - Fundamentals Of Data Science & Analytics","01:00 PM - Lunch","01:30 PM - Association","02:15 PM - Probability & Statistics","03:00 PM - Operating Systems"),
            "Wednesday" to listOf("08:30 AM - Fundamentals Of Data Science & Analytics", "09:30 AM - Machine Language", "10:20 AM - Break", "10:30 AM - Operating Systems", "11:20 AM - Probability & Statistics", "12:10 PM - Environmental Science & Sustainability","01:00 PM - Lunch","01:30 PM - Computer Networks","02:15 PM - Fundamentals Of Data Science & Analytics","03:00 PM - Machine Language"),
            "Thursday" to listOf("08:30 AM - Environmental Science & Sustainability", "09:30 AM - Computer Networks", "10:20 AM - Break", "10:30 AM - DSA/OS Lab", "11:20 AM - DSA/OS Lab", "12:10 PM - Library/PT","01:00 PM - Lunch","01:30 PM - Probability & Statistics","02:15 PM - Machine Language","03:00 PM - Probability & Statistics"),
            "Friday" to listOf("08:30 AM - Operating Systems", "09:30 AM - Environmental Science & Sustainability", "10:20 AM - Break", "10:30 AM - CN/ML Lab", "11:20 AM - CN/ML Lab", "12:10 PM - Skill Rack","01:00 PM - Lunch","01:30 PM - Skill Rack","02:15 PM - OS/ML Lab","03:00 PM - OS/ML Lab"),
            "Saturday" to listOf("08:30 AM - Computer Networks", "09:30 AM - Fundamentals Of Data Science & Analytics", "10:20 AM - Break", "10:30 AM - Operating Systems", "11:20 AM - Fundamentals Of Data Science & Analytics", "12:10 PM - Machine Learning")
        )
    )

    private fun updateTimetable(selectedDay: String) {
        val department = userPreferences.getDepartment()
        val semester = userPreferences.getSemester()
        val key = "${department}_$semester"

        val todaySchedule = timetable[key]?.get(selectedDay) ?: listOf("No classes today!")
        timetableText.text = todaySchedule.joinToString("\n")
    }
}
