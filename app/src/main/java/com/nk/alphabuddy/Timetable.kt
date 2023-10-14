package com.nk.alphabuddy

import android.annotation.SuppressLint
import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import java.text.SimpleDateFormat
import java.util.*

class Timetable : AppCompatActivity() {

    private lateinit var timetableTextView: TextView

    // Define a list of holidays and their descriptions
    private val holidays = mapOf(
        //as per 2024
        "01-01" to "New Year's Day",
        "14-01" to "Bhogi",
        "15-01" to "Surya Pongal",
        "16-01" to "Mattu Pongal",
        "17-01" to "Kaanum Pongal",
        "25-02" to "Thaipusam",
        "08-03" to "Maha Shivratri",
        "19-02" to "Chatrapati Shivaji Maharaj Jayanti",
        "25-03" to "Holi",
        "29-03" to "Good Friday",
        "21-04" to "Mahavir Jayanti",
        "14-04" to "Dr. B. R. Ambedkar Jayanti",
        "01-05" to "Labour Day",
        "10-05" to "Ramadan (Eid al-Fitr)",
        "15-08" to "Independence Day",
        "19-08" to "Raksha Bandhan",
        "07-09" to "Ganesh Chaturthi",
        "02-10" to "Mahatma Gandhi Jayanti",
        "12-10" to "Dussehra",
        "29-10" to "Diwali",
        "15-09" to "Eid-e-Milad",
        "15-11" to "Guru Nanak Jayanti",
        "25-12" to "Christmas Day"
        // Add more holidays as needed, using the format "yyyy-MM-dd"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_timetable)

        timetableTextView = findViewById(R.id.timetableTextView)

    }

    @SuppressLint("SetTextI18n")
    override fun onResume() {
        super.onResume()

        // Automatically detect the current date when the app is resumed
        val currentDate = Calendar.getInstance()
        val currentYear = currentDate.get(Calendar.YEAR)
        val currentMonth = currentDate.get(Calendar.MONTH)
        val currentDay = currentDate.get(Calendar.DAY_OF_MONTH)

        val calendar = Calendar.getInstance()
        calendar.set(currentYear, currentMonth, currentDay)

        val selectedDate = SimpleDateFormat("MM-dd", Locale.US).format(calendar.time)

        val holidayDescription = holidays[selectedDate]

        if (holidayDescription != null) {
            timetableTextView.text = "Today is $holidayDescription"
        } else {
            val dayOfWeek = SimpleDateFormat("EEEE", Locale.US).format(calendar.time)
            val timetableData = getTimetableForDay(dayOfWeek)
            timetableTextView.text = timetableData
        }
    }

    private fun getTimetableForDay(dayOfWeek: String): String {
        val timetableMap = mapOf(
            "Wednesday" to "08:00 AM - 09:15 AM: Physics\n09:15 AM - 10:00 AM: Physics\n10:00 AM - 10:10 AM: Break\n" +
                    "10:10 AM - 11:05 AM: Maths\n11:05 AM - 11:55 AM: Matrices & Calculus\n11:55 AM - 12:45 PM: Problem Solving & Python Programming\n" +
                    "12:45 PM - 01:15 PM: Lunch\n01:15 PM - 02:00 PM: Physics\n02:00 PM - 02:45 PM: Chemistry\n02:45 PM - 03:30 PM: Chemistry",
            // Add entries for other days of the week
        )

        val timetable = timetableMap[dayOfWeek]

        return if (timetable != null) {
            "$dayOfWeek Time Table\n\n$timetable"
        } else {
            "No classes scheduled for $dayOfWeek"
        }
    }
}