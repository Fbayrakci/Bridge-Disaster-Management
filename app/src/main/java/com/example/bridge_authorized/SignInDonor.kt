package com.example.bridge_authorized

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.view.View
import android.text.Editable
import android.text.TextWatcher

import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.widget.AppCompatButton

import androidx.constraintlayout.widget.ConstraintLayout
import com.example.bridge_authorized.databinding.ActivitySignInDonorBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.UUID


class SignInDonor : AppCompatActivity() {
    private lateinit var auth: FirebaseAuth
    private var db = Firebase.firestore

    private lateinit var  binding: ActivitySignInDonorBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySignInDonorBinding.inflate(layoutInflater)
        val view = binding.root
        setContentView(view)

        auth = Firebase.auth

        var fade_in = AnimationUtils.loadAnimation(this,R.anim.fade_in)
        var bottom_down = AnimationUtils.loadAnimation(this,R.anim.bottom_down)

        binding.topLinearLayout.animation = bottom_down

        var handler = Handler()
        var runnable = Runnable{
            binding.cardView2.animation = fade_in
            binding.cardView.animation = fade_in
            binding.textView2.animation = fade_in
            binding.textView3.animation = fade_in
            binding.registerLayout.animation = fade_in
        }

        handler.postDelayed(runnable, 1000)

        binding.donorphonenumber.addTextChangedListener(object : TextWatcher {
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

    fun signinClick(view: View){
        val name = binding.donorname.text.toString()
        val surname = binding.donorsurname.text.toString()
        val email = binding.donoremail.text.toString()
        val password = binding.donorpass.text.toString()
        val confirmPassword = binding.donorpassconfirm.text.toString()
        val tel = binding.donorphonenumber.text.toString()
        if (name.isEmpty() || surname.isEmpty() || email.isEmpty() || password.isEmpty() || confirmPassword.isEmpty() || tel.isEmpty()) {
            Toast.makeText(this, "Please fill in all fields", Toast.LENGTH_LONG).show()
        } else if (!android.util.Patterns.EMAIL_ADDRESS.matcher(email).matches()) {
            Toast.makeText(this, "Invalid email format", Toast.LENGTH_LONG).show()
        } else if (!tel.startsWith("+90")) {
            Toast.makeText(this, "Phone number must start with +90", Toast.LENGTH_LONG).show()
        } else if (password != confirmPassword) {
            Toast.makeText(this, "Passwords do not match", Toast.LENGTH_LONG).show()
        } else {
            val donorMap = hashMapOf(
                "name" to name,
                "surname" to surname,
                "email" to email,
                "password" to password,
                "phone" to tel,

            )
            val donorId = UUID.randomUUID().toString()

            db.collection("donor").document(donorId).set(donorMap)
                .addOnSuccessListener {
                    Toast.makeText(this, "Successfully Added!", Toast.LENGTH_LONG).show()
                    val intent = Intent(this@SignInDonor, donor_dashboard::class.java)
                    startActivity(intent)
                    finish()
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Failed", Toast.LENGTH_LONG).show()
                }
        }
    }



    fun signupClick(view: View){
        val intent = Intent(this@SignInDonor, SignUpDonor::class.java)
        startActivity(intent)
        finish()
    }

}