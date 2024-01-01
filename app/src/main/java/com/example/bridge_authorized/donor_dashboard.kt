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

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonorDashboardBinding.inflate(layoutInflater)
        setContentView(binding.root)



        val bottomNavigationView = findViewById<BottomNavigationView>(R.id.nav_view) // Bu ID'nin layout dosyanızda tanımlı olduğundan emin olun.
        bottomNavigationView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.nav4 -> navigateToActivity(donor_profile::class.java)
                R.id.nav2 -> navigateToActivity(donate_section::class.java)
                R.id.nav3 -> navigateToActivity(donor_see_annoucments::class.java)
                // Diğer menü öğeleri için de benzer navigasyon işlemleri eklenebilir.
            }
            true
        }
    }

    private fun navigateToActivity(targetActivity: Class<*>) {
        val intent = Intent(this, targetActivity)
        startActivity(intent)
    }
}
