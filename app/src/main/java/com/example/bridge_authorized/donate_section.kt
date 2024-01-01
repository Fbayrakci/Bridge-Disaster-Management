package com.example.bridge_authorized

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bridge_authorized.databinding.ActivityDonateSectionBinding
import com.example.bridge_authorized.databinding.ActivitySignInAuthorizedBinding

class donate_section : AppCompatActivity() {
    private lateinit var binding: ActivityDonateSectionBinding
    private lateinit var recyclerView: RecyclerView
    private lateinit var searchView: androidx.appcompat.widget.SearchView
    private var mList: ArrayList<DonationTypeData> = ArrayList()

    private lateinit var adapter: DonationAdapter

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
    }

    private fun addDataToCategory() {
        mList.add(DonationTypeData("Food", R.drawable.annoucments1, each_item_food::class.java))
        mList.add(DonationTypeData("Clothes", R.drawable.annoucments1, each_item_clothes::class.java))
        mList.add(DonationTypeData("Hygiene", R.drawable.annoucments1, each_item_hygene::class.java))
    }
}