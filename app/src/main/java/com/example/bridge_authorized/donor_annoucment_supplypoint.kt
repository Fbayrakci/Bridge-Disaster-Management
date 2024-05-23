package com.example.bridge_authorized

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bridge_authorized.databinding.ActivityDonorAnnoucmentShelterBinding
import com.example.bridge_authorized.databinding.ActivityDonorAnnoucmentSupplypointBinding
import com.google.firebase.firestore.FirebaseFirestore

class donor_annoucment_supplypoint : AppCompatActivity() {

    private lateinit var binding: ActivityDonorAnnoucmentSupplypointBinding
    private lateinit var recyclerView: RecyclerView
    private var mList: ArrayList<AnnoucmentsTypeData> = ArrayList()

    private lateinit var adapter: AnnoucmentsAdapter
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_annoucment_supplypoint)

        binding = ActivityDonorAnnoucmentSupplypointBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerRecentNews
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)


        // Here you add your data to the list
        fetchDataFromFirestore()
        // Initialize the adapter
        adapter = AnnoucmentsAdapter(mList) { position ->
            // No need for an Intent to start an Activity, as the AnnoucmentsTypeData class no longer holds a Class reference.
            val annoucment = mList[position]
        }

        recyclerView.adapter = adapter

        val back = findViewById<ImageView>(R.id.back)

        back.setOnClickListener {
            val intent = Intent(this, donor_annoucment_section::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun fetchDataFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        // Query to fetch only announcements with the category "Recent News"
        db.collection("annoucments")
            .whereEqualTo("category", "Supply Points")
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    // Assuming you have all these fields in your documents
                    val downloadUrl = document.getString("downloadUrl") ?: ""
                    val header = document.getString("header") ?: ""
                    val description = document.getString("description") ?: ""
                    val category = document.getString("category") ?: ""

                    // Add to your list
                    mList.add(AnnoucmentsTypeData(header, downloadUrl, description, category))
                }
                adapter.notifyDataSetChanged()
            }.addOnFailureListener {
                Toast.makeText(this, "Error getting documents: ${it.message}", Toast.LENGTH_SHORT).show()
            }
    }
}