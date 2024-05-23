package com.example.bridge_authorized

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView


class donor_iban : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_iban)



        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener{
            val intent = Intent(this, donor_dashboard::class.java)
            startActivity(intent)
        }

        val afadiban = findViewById<ImageView>(R.id.afadiban)
        afadiban.setOnClickListener{
            Toast.makeText(this, "Iban information is copied", Toast.LENGTH_SHORT).show()
        }

        val ahbapiban = findViewById<ImageView>(R.id.ahbapiban)
        ahbapiban.setOnClickListener{
            Toast.makeText(this, "Iban information is copied", Toast.LENGTH_SHORT).show()
        }

        val kızılayiban = findViewById<ImageView>(R.id.kızılayiban)
        kızılayiban.setOnClickListener{
            Toast.makeText(this, "Iban information is copied", Toast.LENGTH_SHORT).show()
        }

    }
}