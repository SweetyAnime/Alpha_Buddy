package com.nk.alphabuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class SubjectActivity : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SubjectAdapter
    private val subjectList = mutableListOf<String>()
    private val db = FirebaseFirestore.getInstance()
    private val department = "AI" // Change this to dynamically fetch department if needed

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject)

        recyclerView = findViewById(R.id.recyclerViewSubjects)
        recyclerView.layoutManager = LinearLayoutManager(this)
        adapter = SubjectAdapter(subjectList) { subject ->
            val intent = Intent(this, SubjectDetailActivity::class.java)
            intent.putExtra("subject", subject)
            intent.putExtra("department", department)
            startActivity(intent)
        }
        recyclerView.adapter = adapter

        fetchSubjects()
    }

    private fun fetchSubjects() {
        db.collection("notes").document(department)
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val subjectsMap = document.get("subjects") as? Map<*, *>
                    subjectList.clear()
                    subjectList.addAll(subjectsMap?.keys?.map { it.toString() } ?: emptyList())
                    adapter.notifyDataSetChanged()
                } else {
                    Toast.makeText(this, "No subjects found", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching subjects", exception)
            }
    }
}
