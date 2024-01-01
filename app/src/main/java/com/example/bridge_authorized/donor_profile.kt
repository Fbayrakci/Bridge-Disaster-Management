package com.example.bridge_authorized
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore
class donor_profile : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_donor_profile)
        val logoutImageView = findViewById<ImageView>(R.id.logoutDonor)
        logoutImageView.setOnClickListener {
            SessionManager.getInstance(applicationContext).logout()
            val intent = Intent(this, SignUpDonor::class.java)
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
}
