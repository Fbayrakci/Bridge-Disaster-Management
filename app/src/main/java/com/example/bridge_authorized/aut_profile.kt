package com.example.bridge_authorized

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore

class aut_profile : AppCompatActivity() {
    private lateinit var navView: BottomNavigationView
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_aut_profile)

        val annoucment = findViewById<ImageView>(R.id.annoucment)
        annoucment.setOnClickListener{
            val intent = Intent(this, aut_add_annoucments::class.java)
            startActivity(intent)
        }
        navView = findViewById(R.id.nav_view)

        // Bu satırı ekleyin
        navView.selectedItemId = R.id.nav4

        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav1 -> navigateToActivity(authorized_dashboard::class.java)
                R.id.nav2 -> navigateToActivity(seedonations_item::class.java)
                R.id.nav3 -> navigateToActivity(seerequest_item::class.java)
                R.id.nav4 -> navigateToActivity(aut_profile::class.java)
            }
            true
        }

        val logoutImageView = findViewById<ImageView>(R.id.logoutAut)
        logoutImageView.setOnClickListener {
            // Call logout from SessionManager
            SessionManager.getInstance(applicationContext).logout()

            // Redirect to login screen or another appropriate activity
            val intent = Intent(this, SplashScreen::class.java)
            startActivity(intent)
            finish() // Close the current activity


        }

        val sessionManager = SessionManager.getInstance(applicationContext)
        val userId = sessionManager.loggedInUserId

        if (sessionManager.isLoggedIn) {
            val db = FirebaseFirestore.getInstance()
            db.collection("authorized").document(userId!!).get()
                .addOnSuccessListener { document ->
                    runOnUiThread {
                        if (document != null) {
                            findViewById<TextView>(R.id.numberAut).text = document.getString("autphone")
                            findViewById<TextView>(R.id.nameaccAuth).text = document.getString("autcocenter")
                            findViewById<TextView>(R.id.accAuth).text = document.getString("autregion")
                        } else {
                            Toast.makeText(this, "Document not found", Toast.LENGTH_SHORT).show()
                        }
                    }
                }
                .addOnFailureListener { e ->
                    runOnUiThread {
                        Toast.makeText(this, "Error fetching data: $e", Toast.LENGTH_SHORT).show()
                    }
                }
        } else {
            Toast.makeText(this, "No signed-in user", Toast.LENGTH_SHORT).show()
        }
    }
    private fun navigateToActivity(targetActivity: Class<*>) {
        val intent = Intent(this, targetActivity)
        startActivity(intent)
    }
}
