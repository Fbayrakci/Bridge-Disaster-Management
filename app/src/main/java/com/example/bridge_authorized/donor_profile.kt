package com.example.bridge_authorized
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
class donor_profile : AppCompatActivity() {
    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_profile)

        val annoucment = findViewById<ImageView>(R.id.annoucment)
        annoucment.setOnClickListener{
            val intent = Intent(this, donor_annoucment_section::class.java)
            startActivity(intent)
        }

        navView = findViewById(R.id.nav_view)

        navView.selectedItemId = R.id.nav4

        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav1 -> navigateToActivity(donor_dashboard::class.java)
                R.id.nav2 -> navigateToActivity(donate_section::class.java)
                R.id.nav3 -> navigateToActivity(donor_dashboard::class.java)
                R.id.nav4 -> navigateToActivity(donor_profile::class.java)
            }
            true
        }

        val logoutImageView = findViewById<ImageView>(R.id.logoutDonor)
        logoutImageView.setOnClickListener {
            SessionManager.getInstance(applicationContext).logout()
            val intent = Intent(this, SplashScreen::class.java)
            startActivity(intent)
            finish()
        }
        val sessionManager = SessionManager.getInstance(applicationContext)
        val userId = sessionManager.loggedInUserId
        if (sessionManager.isLoggedIn) {
            val db = FirebaseFirestore.getInstance()
            db.collection("donor").document(userId!!).get()
                .addOnSuccessListener { document ->
                    runOnUiThread {
                        if (document != null) {
                            val name = document.getString("name") ?: ""
                            val surname = document.getString("surname") ?: ""
                            val fullName = "$name $surname"

                            findViewById<TextView>(R.id.donorname).text = fullName
                            findViewById<TextView>(R.id.donor_phone).text = document.getString("phone")
                            findViewById<TextView>(R.id.donor_mail).text = document.getString("email")
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
