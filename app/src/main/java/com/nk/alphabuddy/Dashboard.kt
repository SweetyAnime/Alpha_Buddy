package com.nk.alphabuddy

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.View
import android.widget.*
import androidx.appcompat.app.AlertDialog
import androidx.appcompat.app.AppCompatActivity
import com.airbnb.lottie.LottieAnimationView
import com.google.firebase.firestore.FirebaseFirestore
import java.text.SimpleDateFormat
import java.util.*

data class DaySchedule(
    var schedule: List<String> = emptyList()
)

data class TimeTable(
    var monday: DaySchedule = DaySchedule(),
    var tuesday: DaySchedule = DaySchedule(),
    var wednesday: DaySchedule = DaySchedule(),
    var thursday: DaySchedule = DaySchedule(),
    var friday: DaySchedule = DaySchedule(),
    var saturday: DaySchedule = DaySchedule(),
    var sunday: DaySchedule = DaySchedule()
)

class Dashboard : AppCompatActivity() {

    private lateinit var userPreferences: UserPreferences
    private lateinit var timetableOverlay: LinearLayout
    private lateinit var timetableText: TextView
    private lateinit var daySelector: Spinner
    private lateinit var examButton: Button
    private val db = FirebaseFirestore.getInstance()

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

        val daysOfWeek = arrayOf("Monday", "Tuesday", "Wednesday", "Thursday", "Friday", "Saturday", "Sunday")
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

    @SuppressLint("SetTextI18n")
    private fun updateTimetable(selectedDay: String) {
        val department = userPreferences.getDepartment()
        val semester = userPreferences.getSemester()
        val key = "${department}_$semester"

        db.collection("AI_4").document(key)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val dayData = document.get(selectedDay) as? Map<*, *>  // Extract day map
                    val schedule = dayData?.get("schedule") as? List<String>  // Extract "schedule" array

                    if (!schedule.isNullOrEmpty()) {
                        timetableText.text = schedule.joinToString("\n")
                    } else {
                        timetableText.text = "No classes today!"
                    }
                } else {
                    timetableText.text = "No timetable found for this selection."
                }
            }
            .addOnFailureListener { _ ->
                timetableText.text = "Error loading timetable."
            }
    }

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

    private fun updateExamTimetable(selectedExam: String) {
        val examSchedule = examTimetable["AI_4"]?.get(selectedExam) ?: listOf("No exams scheduled!")
        timetableText.text = examSchedule.joinToString("\n")
    }
}
