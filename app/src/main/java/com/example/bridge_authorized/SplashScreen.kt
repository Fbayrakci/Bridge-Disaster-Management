package com.example.bridge_authorized

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.constraintlayout.widget.ConstraintLayout
import com.example.bridge_authorized.databinding.ActivitySignUpDonorBinding
import com.example.bridge_authorized.databinding.ActivitySplashScreenBinding

class SplashScreen : AppCompatActivity() {

    private lateinit var binding: ActivitySplashScreenBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        binding = ActivitySplashScreenBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)
        val donorSign = view.findViewById<Button>(R.id.btnDonor)
        val authorizedSign = view.findViewById<Button>(R.id.btnAuth)

        donorSign.setOnClickListener{
            val intent = Intent(this,SignInDonor::class.java)
            startActivity(intent)
        }
        authorizedSign.setOnClickListener{
            val intent = Intent(this,SignUpAuthorized::class.java)
            startActivity(intent)
        }
    }


}