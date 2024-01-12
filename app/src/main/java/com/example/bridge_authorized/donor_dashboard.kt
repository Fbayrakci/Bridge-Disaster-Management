package com.example.bridge_authorized

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import com.bumptech.glide.Glide
import com.example.bridge_authorized.databinding.ActivityDonorDashboardBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class donor_dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityDonorDashboardBinding
    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonorDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val annoucment = findViewById<ImageView>(R.id.annoucment)
        annoucment.setOnClickListener{
            val intent = Intent(this, donor_annoucment_section::class.java)
            startActivity(intent)
        }

        val goNotice = findViewById<ImageView>(R.id.goNotice)
        goNotice.setOnClickListener{
            val intent = Intent(this, donor_annoucment_section::class.java)
            startActivity(intent)
        }

        val imgDonateSection = findViewById<ImageView>(R.id.imgDonateSection)
        imgDonateSection.setOnClickListener{
            val intent = Intent(this, donate_section::class.java)
            startActivity(intent)
        }

        navView = findViewById(R.id.nav_view)

        // Bu satırı ekleyin
        navView.selectedItemId = R.id.nav1

        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav1 -> navigateToActivity(donor_dashboard::class.java)
                R.id.nav2 -> navigateToActivity(donate_section::class.java)
                R.id.nav3 -> navigateToActivity(donor_iban::class.java)
                R.id.nav4 -> navigateToActivity(donor_profile::class.java)
            }
            true
        }
    }

    private fun navigateToActivity(targetActivity: Class<*>) {
        val intent = Intent(this, targetActivity)
        startActivity(intent)
    }
}
