package com.example.bridge_authorized

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.google.firebase.FirebaseApp

class add_announcement : AppCompatActivity() {
    private lateinit var firebaseApp: FirebaseApp
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_add_announcement)

    }


}