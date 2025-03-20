package com.nk.alphabuddy

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.FirebaseFirestore

class SubjectDetailActivity : AppCompatActivity() {
    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: SubjectAdapter
    private val itemList = mutableListOf<String>()
    private val db = FirebaseFirestore.getInstance()
    private lateinit var subjectName: String

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_subject_detail)

        // Get the subject name from intent
        subjectName = intent.getStringExtra("subject") ?: run {
            Toast.makeText(this, "Invalid subject!", Toast.LENGTH_SHORT).show()
            finish() // Exit activity to prevent a crash
            return
        }


        if (subjectName.isEmpty()) {
            Toast.makeText(this, "Invalid subject!", Toast.LENGTH_SHORT).show()
            finish() // Exit if subject is missing
            return
        }

        recyclerView = findViewById(R.id.recyclerViewSubjects)

        recyclerView.layoutManager = LinearLayoutManager(this)

        adapter = SubjectAdapter(itemList) { selectedItem ->
            openPdfActivity(selectedItem)
        }

        recyclerView.adapter = adapter

        fetchSubjectDetails()
    }

    private fun fetchSubjectDetails() {
        db.collection("notes").document("AI") // Access AI department
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val subjects = document.get("subjects") as? Map<String, Any>
                    val subjectData = subjects?.get(subjectName) as? Map<String, Any>

                    if (subjectData == null) {
                        Toast.makeText(this, "No data found for $subjectName", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }

                    val units = subjectData["units"] as? List<String> ?: emptyList()
                    val pyqs = subjectData["question_papers"] as? List<String> ?: emptyList()

                    itemList.clear()
                    itemList.addAll(units)

                    if (pyqs.isNotEmpty()) {
                        itemList.add("Previous Year Papers")
                        itemList.addAll(pyqs)
                    }

                    adapter.notifyDataSetChanged()
                }
            }
            .addOnFailureListener { exception ->
                Log.e("Firestore", "Error fetching data: ", exception)
                Toast.makeText(this, "Failed to load data", Toast.LENGTH_SHORT).show()
            }
    }

    private fun openPdfActivity(item: String) {
        db.collection("notes").document("AI")
            .get()
            .addOnSuccessListener { document ->
                if (document.exists()) {
                    val subjects = document.get("subjects") as? Map<String, Any>
                    val subjectData = subjects?.get(subjectName) as? Map<String, Any>
                    val pdfLinks = subjectData?.get("pdf_links") as? Map<String, String>

                    val pdfUrl = pdfLinks?.get(item)

                    if (pdfUrl.isNullOrEmpty()) {
                        Toast.makeText(this, "PDF not found!", Toast.LENGTH_SHORT).show()
                        return@addOnSuccessListener
                    }

                    val intent = Intent(this, UnitPdfActivity::class.java)
                    intent.putExtra("pdfUrl", pdfUrl)
                    startActivity(intent)
                }
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to load PDF", Toast.LENGTH_SHORT).show()
            }
    }
}
