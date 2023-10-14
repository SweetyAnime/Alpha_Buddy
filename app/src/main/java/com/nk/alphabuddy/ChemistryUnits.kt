package com.nk.alphabuddy

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class ChemistryUnits : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_chemistry_units)
        supportActionBar?.hide()

        //unit 1 button
        val chemunit1: Button = findViewById(R.id.wateranditstreatment)
        chemunit1.setOnClickListener {
            val intent = Intent(this@ChemistryUnits, ChemSem1Unit1::class.java)
            startActivity(intent)
        }

        //unit 2 button
        val chemunit2: Button = findViewById(R.id.nanochemistry)
        chemunit2.setOnClickListener {
            val intent = Intent(this@ChemistryUnits, ChemSem1Unit2::class.java)
            startActivity(intent)
        }







    }
}