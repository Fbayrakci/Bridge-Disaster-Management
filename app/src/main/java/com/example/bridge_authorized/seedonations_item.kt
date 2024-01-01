package com.example.bridge_authorized

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bridge_authorized.databinding.ActivityDepoBinding
import com.example.bridge_authorized.databinding.ActivitySeedonationsItemBinding
import com.google.firebase.firestore.FirebaseFirestore

class seedonations_item : AppCompatActivity(), SeeDonationTypeAdapter.OnDonationItemClicked {

    private lateinit var binding: ActivitySeedonationsItemBinding

    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeedonationsItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = FirebaseFirestore.getInstance()
        fetchDonations()
    }

    private fun fetchDonations() {
        db.collection("donations")
            .addSnapshotListener { snapshot, exception ->
                if (exception != null) {
                    Log.e("FirebaseFirestore", "Error fetching donations: ${exception.localizedMessage}")
                    Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show()
                } else {
                    if (snapshot != null && !snapshot.isEmpty) {
                        val donations = mutableListOf<SeeDonationType>()
                        for (document in snapshot.documents) {
                            val donationType = document.toObject(SeeDonationType::class.java)
                            if (donationType != null) {
                                donations.add(donationType)
                            }
                        }
                        setupRecyclerView(donations)
                    } else {
                        Log.d("FirebaseFirestore", "No donations found")
                    }
                }
            }
    }

    private fun setupRecyclerView(donations: List<SeeDonationType>) {
        val adapter = SeeDonationTypeAdapter(donations, this)
        binding.recyclerViewSeeDonations.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSeeDonations.adapter = adapter
    }

    override fun onItemReceived(donationType: SeeDonationType) {
        if (donationType.donationItems.isNotEmpty()) {
            val firstDonationItem = donationType.donationItems[0]
            val category = firstDonationItem["category"]
            val item = firstDonationItem["item"]
            val quantity = firstDonationItem["quantity"]

            if (category == "Food" && item == "Water(12 pieces)") {
                Log.d("seedonations_item", "Sending broadcast with quantity: $quantity")
                val intent = Intent("com.example.bridge_authorized.UPDATE_QUANTITY")
                intent.putExtra("quantity", quantity)
                sendBroadcast(intent)
            }
        }
    }



}
