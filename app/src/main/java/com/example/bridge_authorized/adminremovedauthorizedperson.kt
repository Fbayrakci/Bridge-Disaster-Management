package com.example.bridge_authorized

import android.annotation.SuppressLint
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class adminremovedauthorizedperson : AppCompatActivity() {

    private lateinit var recyclerView: RecyclerView
    private lateinit var adapter: AuthorizedPersonAdapter
    private val db = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_adminremovedauthorizedperson)

        val back = findViewById<ImageView>(R.id.back)
        back.setOnClickListener{
            val intent = Intent(this, admin_dashboard::class.java)
            startActivity(intent)
        }
        recyclerView = findViewById(R.id.recyclerViewDeleteAuthorized)
        recyclerView.layoutManager = LinearLayoutManager(this)
        fetchUsers()
    }

    private fun fetchUsers() {
        db.collection("authorized") // Make sure this matches the exact name in Firestore
            .whereEqualTo("isAdmin", false) // Filter to get only non-admin users
            .get()
            .addOnSuccessListener { documents ->
                val usersList = documents.mapNotNull { document ->
                    document.toObject(AuthorizedPerson::class.java).apply {
                        id = document.id // Set the document ID
                    }
                }
                Log.d("adminremovedauthorizedperson", "Fetched ${usersList.size} users")

                // Optional: Log some user details
                usersList.forEach { user ->
                    Log.d("adminremovedauthorizedperson", "User: ${user.autname}")
                }

                adapter = AuthorizedPersonAdapter(usersList) { user ->
                    deleteUser(user)
                }
                recyclerView.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.e("adminremovedauthorizedperson", "Error getting documents", exception)
                Toast.makeText(this, "Error getting documents: $exception", Toast.LENGTH_SHORT).show()
            }
    }



    private fun deleteUser(user: AuthorizedPerson) {
        db.collection("authorized").document(user.id)
            .delete()
            .addOnSuccessListener {
                Toast.makeText(this, "User deleted", Toast.LENGTH_SHORT).show()
                fetchUsers() // Refresh the list
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error deleting user", Toast.LENGTH_SHORT).show()
            }
    }
}
