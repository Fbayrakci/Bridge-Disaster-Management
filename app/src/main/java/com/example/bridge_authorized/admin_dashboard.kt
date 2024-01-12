package com.example.bridge_authorized

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton
import com.example.bridge_authorized.databinding.ActivityAdminDashboardBinding
import com.example.bridge_authorized.databinding.ActivitySignInAuthorizedBinding

class admin_dashboard : AppCompatActivity() {
    private lateinit var  binding: ActivityAdminDashboardBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_admin_dashboard)
        binding = ActivityAdminDashboardBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        val imgAddPerson = view.findViewById<ImageView>(R.id.imgAddPerson)
        val imageView4 = view.findViewById<ImageView>(R.id.imageView4)
        val logoutImageView = findViewById<ImageView>(R.id.logoutAut)
        logoutImageView.setOnClickListener {
            SessionManager.getInstance(applicationContext).logout()
            val intent = Intent(this, SplashScreen::class.java)
            startActivity(intent)
            finish()
        }

        // Butona tıklama dinleyicisi ekleme
        imgAddPerson.setOnClickListener {
            // Başka bir Activity'yi başlatma
            val intent = Intent(this, SignInAuthorized::class.java)
            startActivity(intent)
        }

        imageView4.setOnClickListener {
            // Başka bir Activity'yi başlatma
            val intent = Intent(this, adminremovedauthorizedperson::class.java)
            startActivity(intent)
        }
    }


    fun logoutUser() {
        val sharedPreferences = getSharedPreferences("AppPrefsaut", MODE_PRIVATE)
        sharedPreferences.edit().remove("IsLoggedInaut").apply()

        // Kullanıcı çıkış yaptıktan sonra giriş ekranına yönlendirme
        val intent = Intent(this, SignUpAuthorized::class.java)
        startActivity(intent)
        finish()

        Toast.makeText(this, "Çıkış yapıldı", Toast.LENGTH_SHORT).show()
    }
}