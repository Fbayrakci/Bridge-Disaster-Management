package com.example.bridge_authorized

import android.annotation.SuppressLint
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide

class authorized_dashboard : AppCompatActivity() {
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorized_dashboard)
        val imageView = findViewById<ImageView>(R.id.yetkili)

        // Glide ile GIF'i ImageView'a yükleme
        Glide.with(this)
            .load(R.drawable.yetkili)
            .into(imageView)
    }

}