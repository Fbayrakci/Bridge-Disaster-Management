package com.example.bridge_authorized

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.bridge_authorized.databinding.ActivitySignUpAuthorizedBinding
import com.google.firebase.firestore.FirebaseFirestore

class SignUpAuthorized : AppCompatActivity() {
    private lateinit var binding: ActivitySignUpAuthorizedBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignUpAuthorizedBinding.inflate(layoutInflater)
        setContentView(binding.root)

        setupAnimations()

        // Oturum kontrolü
        val sessionManager = SessionManager.getInstance(applicationContext)
        if (sessionManager.isLoggedIn) {
            // Kullanıcı oturum açmış, ilgili dashboard'a yönlendir
            navigateToDashboardBasedOnRole()
            return
        }

        // Giriş işlemleri
        binding.authLoginAuthorized.setOnClickListener {
            val userEmail = binding.loginAuth.text.toString().trim()
            val userPassword = binding.passAuth.text.toString().trim()
            if (userEmail.isNotEmpty() && userPassword.isNotEmpty()) {
                loginUser(userEmail, userPassword)
            } else {
                Toast.makeText(this, "Lütfen geçerli e-posta ve şifre girin", Toast.LENGTH_SHORT).show()
            }
        }
    }

    private fun setupAnimations() {
        val fade_in = AnimationUtils.loadAnimation(this, R.anim.fade_in)
        val bottom_down = AnimationUtils.loadAnimation(this, R.anim.bottom_down)
        binding.topLinearLayout.animation = bottom_down
        Handler().postDelayed({
            binding.cardView2.animation = fade_in
            binding.cardView.animation = fade_in
            binding.textView2.animation = fade_in
        }, 1000)
    }

    private fun loginUser(email: String, password: String) {
        val db = FirebaseFirestore.getInstance()
        val usersCollection = db.collection("authorized")
        usersCollection.whereEqualTo("autemail", email).whereEqualTo("auttcpass", password)
            .get().addOnCompleteListener { task ->
                if (task.isSuccessful && task.result != null && !task.result!!.isEmpty) {
                    val userDocument = task.result!!.documents[0]
                    val userId = userDocument.id
                    val isAdmin = userDocument.getBoolean("isAdmin") ?: false

                    // SessionManager'a kullanıcı ID'sini ve admin durumunu kaydet
                    val sessionManager = SessionManager.getInstance(applicationContext)
                    sessionManager.loggedInUserId = userId
                    sessionManager.isLoggedIn = true
                    sessionManager.isAdmin = isAdmin

                    navigateToDashboardBasedOnRole()
                } else {
                    Toast.makeText(this, "Geçersiz e-posta veya şifre", Toast.LENGTH_SHORT).show()
                }
            }
    }

    private fun navigateToDashboardBasedOnRole() {
        val sessionManager = SessionManager.getInstance(applicationContext)
        val destination = if (sessionManager.isAdmin) admin_dashboard::class.java else authorized_dashboard::class.java
        val intent = Intent(this, destination)
        startActivity(intent)
        finish()
    }
}
