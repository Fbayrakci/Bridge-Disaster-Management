package com.example.bridge_authorized

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bridge_authorized.databinding.ActivityDonorSeeAnnoucmentsBinding
import com.google.firebase.firestore.FirebaseFirestore

class donor_see_annoucments : AppCompatActivity() {
    private lateinit var binding: ActivityDonorSeeAnnoucmentsBinding
    private lateinit var recyclerView: RecyclerView
    private var mList: ArrayList<AnnoucmentsTypeData> = ArrayList()

    private lateinit var adapter: AnnoucmentsAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivityDonorSeeAnnoucmentsBinding.inflate(layoutInflater)
        setContentView(binding.root)

        recyclerView = binding.recyclerViewAnnoucments
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        val backdash = findViewById<ImageView>(R.id.backdash)
        backdash.setOnClickListener{
            val intent = Intent(this, donor_dashboard::class.java)
            startActivity(intent)
        }
        // Here you add your data to the list
        fetchDataFromFirestore()
        // Initialize the adapter
        adapter = AnnoucmentsAdapter(mList) { position ->
            // No need for an Intent to start an Activity, as the AnnoucmentsTypeData class no longer holds a Class reference.
            val annoucment = mList[position]
        }

        recyclerView.adapter = adapter
    }


    private fun fetchDataFromFirestore() {
        val db = FirebaseFirestore.getInstance()
        db.collection("annoucments").get().addOnSuccessListener { documents ->
            for (document in documents) {
                val downloadUrl = document.getString("downloadUrl") ?: ""
                val comment = document.getString("comment") ?: ""
                mList.add(AnnoucmentsTypeData(comment, downloadUrl))
            }
            adapter.notifyDataSetChanged()
        }.addOnFailureListener {
            Toast.makeText(this, "Error getting documents: ${it.message}", Toast.LENGTH_SHORT).show()
        }
    }

}
