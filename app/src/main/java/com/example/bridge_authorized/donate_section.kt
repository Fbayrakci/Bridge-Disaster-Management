package com.example.bridge_authorized

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bridge_authorized.databinding.ActivityDonateSectionBinding
import com.example.bridge_authorized.databinding.ActivitySignInAuthorizedBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class donate_section : AppCompatActivity() {
    private lateinit var binding: ActivityDonateSectionBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private var mList: ArrayList<DonationTypeData> = ArrayList()

    private lateinit var adapter: DonationAdapter
    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donate_section)

        binding = ActivityDonateSectionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        recyclerView = view.findViewById(R.id.recyclerViewDonateType)
        searchView = view.findViewById(R.id.searchDonateType) as androidx.appcompat.widget.SearchView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        mList = ArrayList()
        addDataToCategory()

        // Initialize the adapter with onItemClicked implementation
        adapter = DonationAdapter(mList) { activityClass ->
            val intent = Intent(this, activityClass)
            startActivity(intent)
        }

        recyclerView.adapter = adapter

        val annoucment = findViewById<ImageView>(R.id.annoucment)
        annoucment.setOnClickListener{
            val intent = Intent(this, donor_annoucment_section::class.java)
            startActivity(intent)
        }

        navView = findViewById(R.id.nav_view)

        navView.selectedItemId = R.id.nav2

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
    private fun addDataToCategory() {
        mList.add(DonationTypeData("Food", R.drawable.sectionfood, each_item_food::class.java))
        mList.add(DonationTypeData("Clothes", R.drawable.sectioncloth, each_item_clothes::class.java))
        mList.add(DonationTypeData("Hygiene", R.drawable.sectionhygene, each_item_hygene::class.java))
    }
}
