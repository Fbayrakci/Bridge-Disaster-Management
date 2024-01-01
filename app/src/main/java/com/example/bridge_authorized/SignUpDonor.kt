package com.example.bridge_authorized

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.bridge_authorized.databinding.ActivitySignUpDonorBinding
import com.google.firebase.firestore.FirebaseFirestore

class SignUpDonor : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpDonorBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpDonorBinding.inflate(layoutInflater)
        setContentView(binding.root)

        // Oturum kontrolü
        val sessionManager = SessionManager.getInstance(applicationContext)
        if (sessionManager.isLoggedIn) {
            // Kullanıcı oturum açmış, ilgili dashboard'a yönlendir
            navigateToDashboard()
            return
        }

        // Giriş işlemleri
        binding.donorSignUpDashboard.setOnClickListener {
            val userEmail = binding.donoremailsign.text.toString().trim()
            val userPassword = binding.donorpasssign.text.toString().trim()
            if (userEmail.isNotEmpty() && userPassword.isNotEmpty()) {
                loginUser(userEmail, userPassword)
            } else {
                Toast.makeText(this, "Lütfen geçerli e-posta ve şifre girin", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun loginUser(email: String, password: String) {
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("donor")
        usersCollection.whereEqualTo("email", email).whereEqualTo("password", password)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null && !task.result!!.isEmpty) {
                    val userDocument = task.result!!.documents[0]
                    val userId = userDocument.id

                    // SessionManager'a kullanıcı ID'sini kaydet
                    SessionManager.getInstance(applicationContext).apply {
                        loggedInUserId = userId
                        isLoggedIn = true
                        isAdmin = false // Donorlar için isAdmin false olarak ayarlanır
                    }

                    navigateToDashboard()
                } else {
                    Toast.makeText(this, "Geçersiz e-posta veya şifre", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToDashboard() {
        val intent = Intent(this, donor_dashboard::class.java)
        startActivity(intent)
        finish()
    }
}
