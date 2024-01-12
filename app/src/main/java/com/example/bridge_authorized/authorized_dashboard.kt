package com.example.bridge_authorized

import android.annotation.SuppressLint
import android.app.Activity
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Layout
import android.widget.ImageView
import androidx.constraintlayout.widget.ConstraintLayout
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.bumptech.glide.Glide
import com.example.bridge_authorized.databinding.ActivityAuthorizedDashboardBinding
import com.example.bridge_authorized.databinding.ActivityCardBinding
import com.google.android.material.bottomnavigation.BottomNavigationItemView
import com.google.android.material.bottomnavigation.BottomNavigationMenuView
import com.google.android.material.bottomnavigation.BottomNavigationView

class authorized_dashboard : AppCompatActivity() {
    private lateinit var binding: ActivityAuthorizedDashboardBinding
    private lateinit var navView: BottomNavigationView
    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_authorized_dashboard)
        val imageView = findViewById<ImageView>(R.id.yetkili)
        binding = ActivityAuthorizedDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        setContentView(binding.root)
        // Glide ile GIF'i ImageView'a yükleme
        Glide.with(this)
            .load(R.drawable.yetkili)
            .into(imageView)

        val depo = findViewById<ConstraintLayout>(R.id.constraintLayout)
        depo.setOnClickListener{
            val intent = Intent(this, depo_activity::class.java)
            startActivity(intent)
        }


        val seeDonations = findViewById<ImageView>(R.id.seeDonations)
        seeDonations.setOnClickListener{
            val intent = Intent(this, seedonations_item::class.java)
            startActivity(intent)
        }

        val requestHelp = findViewById<ImageView>(R.id.requestHelpImg)
        requestHelp.setOnClickListener{
            val intent = Intent(this, reguest_help_form::class.java)
            startActivity(intent)
        }

        val seeRequest = findViewById<ImageView>(R.id.seeRequest)
        seeRequest.setOnClickListener{
            val intent = Intent(this, seerequest_item::class.java)
            startActivity(intent)
        }

        val addannoucments = findViewById<ImageView>(R.id.imageViewAddAnnoucments)
        addannoucments.setOnClickListener{
            val intent = Intent(this, aut_add_annoucments::class.java)
            startActivity(intent)
        }

        val annoucment = findViewById<ImageView>(R.id.annoucment)
        annoucment.setOnClickListener{
            val intent = Intent(this, aut_add_annoucments::class.java)
            startActivity(intent)
        }




        navView = findViewById(R.id.nav_view)

        // Bu satırı ekleyin
        navView.selectedItemId = R.id.nav1

        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav1 -> navigateToActivity(authorized_dashboard::class.java)
                R.id.nav2 -> navigateToActivity(seedonations_item::class.java)
                R.id.nav3 -> navigateToActivity(seerequest_item::class.java)
                R.id.nav4 -> navigateToActivity(aut_profile::class.java)
            }
            true
        }

    }



    private fun navigateToActivity(targetActivity: Class<*>) {
        val intent = Intent(this, targetActivity)
        startActivity(intent)
    }

}

