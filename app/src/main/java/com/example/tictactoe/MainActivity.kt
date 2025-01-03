package com.example.tictactoe

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button

class MainActivity : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val start_btn = findViewById<Button>(R.id.start_btn)
        start_btn.setOnClickListener {
            val intent = Intent(this,GameScreen::class.java)
            startActivity(intent)
            finish()
        }

        val info_btn = findViewById<Button>(R.id.info_btn)
        info_btn.setOnClickListener {
            val intent = Intent(this,InfoScreen::class.java)
            startActivity(intent)
            finish()
        }
    }
}