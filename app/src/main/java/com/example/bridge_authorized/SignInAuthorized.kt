package com.example.bridge_authorized

import android.content.Intent
import android.net.Uri
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.text.Editable
import android.text.TextWatcher
import android.view.View

import android.view.animation.AnimationUtils
import android.widget.Button
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton

import androidx.constraintlayout.widget.ConstraintLayout
import com.example.bridge_authorized.databinding.ActivitySignInAuthorizedBinding
import com.example.bridge_authorized.databinding.ActivitySignInDonorBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.File
import java.lang.Exception
import java.util.UUID


class SignInAuthorized : AppCompatActivity() {

    private lateinit var  binding: ActivitySignInAuthorizedBinding
    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInAuthorizedBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)


        var fade_in = AnimationUtils.loadAnimation(this,R.anim.fade_in)
        var bottom_down = AnimationUtils.loadAnimation(this,R.anim.bottom_down)

        binding.topLinearLayout.animation = bottom_down

        var handler = Handler()
        var runnable = Runnable{
            binding.cardView2.animation = fade_in
            binding.textView2.animation = fade_in
        }

        handler.postDelayed(runnable, 1000)

        binding.autphonenumber.addTextChangedListener(object : TextWatcher {
            var isFormatting = false
            var lastFormattedString = ""

            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {
                // İhtiyaç duyulursa burada bir şeyler yapılabilir
            }

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                // İhtiyaç duyulursa burada bir şeyler yapılabilir
            }

            override fun afterTextChanged(s: Editable?) {
                if (isFormatting || s == null) return

                isFormatting = true

                // Sadece rakamları al
                val digits = s.replace(Regex("[^\\d]"), "")

                val formatted = buildString {
                    append("+90")

                    if (digits.length > 2) {
                        append(" ")
                        append(digits.substring(2, minOf(digits.length, 5)))
                    }

                    if (digits.length > 5) {
                        append(" ")
                        append(digits.substring(5, minOf(digits.length, 8)))
                    }

                    if (digits.length > 8) {
                        append(" ")
                        append(digits.substring(8, minOf(digits.length, 10)))
                    }

                    if (digits.length > 10) {
                        append(" ")
                        append(digits.substring(10, minOf(digits.length, 12)))
                    }
                }

                // Eğer formatlanmış metin öncekiyle farklıysa, güncelle
                if (formatted != lastFormattedString) {
                    lastFormattedString = formatted
                    s.replace(0, s.length, formatted)
                }

                isFormatting = false
            }
        })


    }

    fun signUPClick(view: View){
        val autname = binding.autname.text.toString()
        val autsurname = binding.autsurname.text.toString()
        val autemail = binding.autemail.text.toString()
        val auttcpassword = binding.autTCpass.text.toString()
        val auttel = binding.autphonenumber.text.toString()
        val autregion = binding.autRegionCenter.text.toString()
        val autcocenter = binding.autCoordinationCenter.text.toString()
        val isAdmin = false
        if (autname.isEmpty() || autsurname.isEmpty() || autemail.isEmpty() || auttcpassword.isEmpty() || auttel.isEmpty() || autregion.isEmpty() || autcocenter.isEmpty() ) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(autemail).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_LONG).show()
        } else if (!auttel.startsWith("+90")) {
            Toast.makeText(this, "Phone number must start with +90", Toast.LENGTH_LONG).show()
        }
        else {
            val authMap = hashMapOf(
                "autname" to autname,
                "autsurname" to autsurname,
                "autemail" to autemail,
                "auttcpass" to auttcpassword,
                "autphone" to auttel,
                "autregion" to autregion,
                "autcocenter" to autcocenter,
                "isAdmin" to isAdmin
            )
            val autId = UUID.randomUUID().toString()
            db.collection("authorized").document(autId).set(authMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully Added!", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@SignInAuthorized, admin_dashboard::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                }
        }
    }



}