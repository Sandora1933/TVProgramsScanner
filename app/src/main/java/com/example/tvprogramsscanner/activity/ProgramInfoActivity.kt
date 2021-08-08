package com.example.tvprogramsscanner.activity

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.tvprogramsscanner.R

class ProgramInfoActivity : AppCompatActivity() {

    lateinit var descriptionTextView: TextView
    lateinit var descriptionTitleTextView: TextView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_program_info)

        descriptionTitleTextView = findViewById(R.id.descriptionTitleTextView)
        descriptionTextView = findViewById(R.id.descriptionTextView)

        val intent = intent
        val programDescription = intent.getStringExtra("description")

        descriptionTextView.text = programDescription
    }
}