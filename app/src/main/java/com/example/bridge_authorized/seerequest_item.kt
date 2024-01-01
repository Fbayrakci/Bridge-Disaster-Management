package com.example.bridge_authorized

import SeeRequestAdapter
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bridge_authorized.databinding.ActivitySeedonationsItemBinding
import com.example.bridge_authorized.databinding.ActivitySeerequestItemBinding
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class seerequest_item : AppCompatActivity() {

    private lateinit var binding: ActivitySeerequestItemBinding
    private var db = FirebaseFirestore.getInstance()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeerequestItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Firebase.firestore
        fetchRequests()
    }


    private fun fetchRequests() {
        db.collection("requesthelp") // Firestore koleksiyon adınız
            .get()
            .addOnSuccessListener { documents ->
                val requestsList = documents.mapNotNull { document ->
                    document.toObject(SeeRequestItem::class.java)
                }
                setupRecyclerView(requestsList)
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error getting documents: ", exception)
            }
    }

    private fun setupRecyclerView(requests: List<SeeRequestItem>) {
        val adapter = SeeRequestAdapter(requests)
        binding.recyclerViewSeeRequest.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSeeRequest.adapter = adapter

        Log.d("RecyclerView", "RecyclerView setup with ${requests.size} items")
    }
}
