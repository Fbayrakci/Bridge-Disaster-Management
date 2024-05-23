package com.example.bridge_authorized

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ArrayAdapter
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bridge_authorized.databinding.ActivityDonorAnnoucmentSectionBinding
import com.google.android.material.bottomnavigation.BottomNavigationView

class donor_annoucment_section : AppCompatActivity() {
    private lateinit var binding: ActivityDonorAnnoucmentSectionBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private var mListan: ArrayList<DonorAnnoucmentTypeData> = ArrayList()

    private lateinit var adapter: DonorAnnoucmentAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_annoucment_section)


        binding = ActivityDonorAnnoucmentSectionBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        recyclerView = view.findViewById(R.id.recyclerViewDonateType)
        searchView = view.findViewById(R.id.searchDonateType) as androidx.appcompat.widget.SearchView
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        mListan = ArrayList()
        addDataToCategory()

        // Initialize the adapter with onItemClicked implementation
        adapter = DonorAnnoucmentAdapter(mListan) { activityClass ->
            val intent = Intent(this, activityClass)
            startActivity(intent)
        }

        recyclerView.adapter = adapter


        val back = findViewById<ImageView>(R.id.back)

        back.setOnClickListener {
            val intent = Intent(this, donor_dashboard::class.java)
            startActivity(intent)
            finish()
        }
        // Bu satırı ekleyin
    }
    private fun navigateToActivity(targetActivity: Class<*>) {
        val intent = Intent(this, targetActivity)
        startActivity(intent)
    }
    private fun addDataToCategory() {
        mListan.add(DonorAnnoucmentTypeData("Recent News", R.drawable.recentnews, donor_annoncment_recentnews::class.java))
        mListan.add(DonorAnnoucmentTypeData("Shelter", R.drawable.shellter, donor_annoucment_shelter::class.java))
        mListan.add(DonorAnnoucmentTypeData("Supply Points", R.drawable.supplypoints, donor_annoucment_supplypoint::class.java))
        mListan.add(DonorAnnoucmentTypeData("Health Center", R.drawable.healthcenter, donor_annoucment_healthcenter::class.java))
    }
}