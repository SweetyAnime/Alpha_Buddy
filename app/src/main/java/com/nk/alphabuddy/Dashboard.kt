package com.nk.alphabuddy

import android.annotation.SuppressLint
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
    private lateinit var examButton: Button

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_dashboard)

        userPreferences = UserPreferences(this)
        supportActionBar?.hide()

        if (userPreferences.getDepartment() == null || userPreferences.getSemester() == null) {
            showDepartmentSelectionDialog()
        }

        val timetableButton = findViewById<LottieAnimationView>(R.id.timetableButton)
        timetableOverlay = findViewById(R.id.timetableOverlay)
        timetableText = findViewById(R.id.timetableText)
        daySelector = findViewById(R.id.daySelector)
        val closeOverlayButton = findViewById<Button>(R.id.closeOverlayButton)
        examButton = findViewById(R.id.examButtonnew)

        timetableButton.setOnClickListener {
            timetableOverlay.visibility = View.VISIBLE
        }

        closeOverlayButton.setOnClickListener {
            timetableOverlay.visibility = View.GONE
        }

        examButton.setOnClickListener {
            showExamSelectionDialog()
        }

        val daysOfWeek = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday","Sunday")
        val adapter = ArrayAdapter(this, android.R.layout.simple_spinner_dropdown_item, daysOfWeek)
        daySelector.adapter = adapter

        val currentDay = SimpleDateFormat("EEEE", Locale.getDefault()).format(Date())
        val defaultPosition = daysOfWeek.indexOf(currentDay)
        if (defaultPosition != -1) {
            daySelector.setSelection(defaultPosition)
            updateTimetable(currentDay)
        }

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

    private fun showExamSelectionDialog() {
        val exams = arrayOf("CAT-1", "CAT-2", "CAT-3", "M-1", "M-2", "Practicals", "Semester")

        AlertDialog.Builder(this)
            .setTitle("Select Exam Type")
            .setItems(exams) { _, which ->
                val selectedExam = exams[which]
                updateExamTimetable(selectedExam)
            }
            .show()
    }

    private val timetable = mapOf(
        "AI_4" to mapOf(
            "Sunday" to listOf("Holiday"),
            "Monday" to listOf("08:30 AM - DSA/CN Lab", "09:30 AM - DSA/CN Lab", "10:20 AM - Break", "10:30 AM - Machine Language", "11:20 AM - Probability & Statistics", "12:10 PM - Operating Systems","01:00 PM - Lunch","01:30 PM - Environmental Science & Sustainability","02:15 PM - Skill Rack","03:00 PM - Skill Rack"),
            "Tuesday" to listOf("08:30 AM - Operating Systems", "09:30 AM - Probability & Statistics", "10:20 AM - Break", "10:30 AM - Machine Language", "11:20 AM - Environmental Science & Sustainability", "12:10 PM - Fundamentals Of Data Science & Analytics","01:00 PM - Lunch","01:30 PM - Machine Language","02:15 PM - Probability & Statistics","03:00 PM - Computer Networks"),
            "Wednesday" to listOf("08:30 AM - Fundamentals Of Data Science & Analytics/NM", "09:30 AM - Association/NM", "10:20 AM - Break", "10:30 AM - Operating Systems/NM", "11:20 AM - Probability & Statistics/NM", "12:10 PM - Environmental Science & Sustainability/NM","01:00 PM - Lunch","01:30 PM - Computer Networks/NM","02:15 PM - Fundamentals Of Data Science & Analytics/NM","03:00 PM - Machine Language/NM"),
            "Thursday" to listOf("08:30 AM - Environmental Science & Sustainability", "09:30 AM - Computer Networks", "10:20 AM - Break", "10:30 AM - DSA/OS Lab", "11:20 AM - DSA/OS Lab", "12:10 PM - Library/PT","01:00 PM - Lunch","01:30 PM - Probability & Statistics","02:15 PM - Machine Language","03:00 PM - Probability & Statistics"),
            "Friday" to listOf("08:30 AM - Fundamentals Of Data Science & Analytics", "09:30 AM - Environmental Science & Sustainability", "10:20 AM - Break", "10:30 AM - CN/ML Lab", "11:20 AM - CN/ML Lab", "12:10 PM - Operating Systems","01:00 PM - Lunch","01:30 PM - Computer Networks","02:15 PM - OS/ML Lab","03:00 PM - OS/ML Lab"),
            "Saturday" to listOf("08:30 AM - Computer Networks", "09:30 AM - Fundamentals Of Data Science & Analytics", "10:20 AM - Break", "10:30 AM - Operating Systems", "11:20 AM - Fundamentals Of Data Science & Analytics", "12:10 PM - Machine Learning"),
        )
    )

    private val examTimetable = mapOf(
        "AI_4" to mapOf(
            "CAT-1" to listOf("March 5 - DSA", "March 6 - Statistics", "March 7 - OS"),
            "CAT-2" to listOf("April 10 - DSA", "April 11 - CN", "April 12 - ML"),
            "CAT-3" to listOf("May 15 - Data Science", "May 16 - OS", "May 17 - Statistics"),
            "M-1" to listOf("May 25 - OS", "May 26 - CN"),
            "M-2" to listOf("June 10 - ML", "June 11 - Statistics"),
            "Practicals" to listOf("June 15 - DSA Lab", "June 16 - CN/ML Lab"),
            "Semester" to listOf("July 1 - OS", "July 2 - Data Science", "July 3 - Statistics")
        )
    )

    private fun updateTimetable(selectedDay: String) {
        val department = userPreferences.getDepartment()
        val semester = userPreferences.getSemester()
        val key = "${department}_$semester"

        val todaySchedule = timetable[key]?.get(selectedDay) ?: listOf("No classes today!")
        timetableText.text = todaySchedule.joinToString("\n")
    }

    private fun updateExamTimetable(selectedExam: String) {
        val department = userPreferences.getDepartment()
        val semester = userPreferences.getSemester()
        val key = "${department}_$semester"

        val examSchedule = examTimetable[key]?.get(selectedExam) ?: listOf("No exams scheduled!")
        timetableText.text = examSchedule.joinToString("\n")
    }
}
