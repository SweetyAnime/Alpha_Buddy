package com.nk.alphabuddy

import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.nk.alphabuddy.Event
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

class Events : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var eventAdapter: EventAdapter
    private val eventList = ArrayList<Event>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_events)
        supportActionBar?.hide()

        recyclerView = findViewById(R.id.recyclerViewEvents)
        recyclerView.layoutManager = LinearLayoutManager(this)
        eventAdapter = EventAdapter(eventList)
        recyclerView.adapter = eventAdapter

        fetchEventsFromFirestore()
    }

    private fun fetchEventsFromFirestore() {
        val db = FirebaseFirestore.getInstance()

        // Get today's date at midnight (00:00:00)
        val today = Calendar.getInstance().apply {
            set(Calendar.HOUR_OF_DAY, 0)
            set(Calendar.MINUTE, 0)
            set(Calendar.SECOND, 0)
            set(Calendar.MILLISECOND, 0)
        }.time

        db.collection("events")
            .whereGreaterThanOrEqualTo("date", today) // Only fetch future events
            .orderBy("date", Query.Direction.ASCENDING) // Sort by date
            .get()
            .addOnSuccessListener { documents ->
                eventList.clear()
                for (doc in documents) {
                    val name = doc.getString("name") ?: "Unknown Event"
                    val imageURL = doc.getString("imageURL") ?: ""

                    val timestamp = doc.getTimestamp("date")
                    val dateString = timestamp?.toDate()?.let {
                        SimpleDateFormat("yyyy-MM-dd", Locale.getDefault()).format(it)
                    } ?: "0000-00-00"

                    val remainingDays = calculateDaysRemaining(dateString)

                    eventList.add(Event(name, imageURL, dateString, remainingDays))
                }
                eventAdapter.notifyDataSetChanged()
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load events", Toast.LENGTH_SHORT).show()
            }
    }


    private fun calculateDaysRemaining(eventDate: String): String {
        return try {
            val sdf = SimpleDateFormat("yyyy-MM-dd", Locale.getDefault())
            val eventDateParsed = sdf.parse(eventDate)
            val now = Calendar.getInstance().time

            val diffMillis = eventDateParsed.time - now.time
            val diffDays = (diffMillis / (1000 * 60 * 60 * 24)).toInt()

            when {
                diffDays > 1 -> "Starts in $diffDays days"
                diffDays == 1 -> "Starts in 1 day"
                diffDays == 0 -> {
                    val diffHours = (diffMillis / (1000 * 60 * 60)).toInt()
                    val diffMinutes = ((diffMillis % (1000 * 60 * 60)) / (1000 * 60)).toInt()
                    if (diffHours > 0) {
                        "Starts in $diffHours hours"
                    } else {
                        "Starts in $diffMinutes minutes"
                    }
                }
                else -> "Event ended"
            }
        } catch (e: Exception) {
            "Invalid date"
        }
    }

}
