package com.example.bridge_authorized

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import com.example.bridge_authorized.databinding.ActivityDonorFormBinding
import com.example.bridge_authorized.databinding.ActivityRequestHelpFormBinding
import com.example.bridge_authorized.databinding.ActivitySignUpAuthorizedBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class reguest_help_form : AppCompatActivity() {
    private lateinit var binding: ActivityRequestHelpFormBinding
    private var db = Firebase.firestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRequestHelpFormBinding.inflate(layoutInflater)
        setContentView(binding.root)


        val categories = resources.getStringArray(R.array.categories)
        val adapter = ArrayAdapter(this, R.layout.list_item, categories)
        binding.autoCompleteCategory.setAdapter(adapter)

        // Kategorilere göre item'lar
        val foodItems = arrayOf("Water(12 pieces)", "Canned Food(1 packet)", "Legumes")
        val clothesItems = arrayOf("Coat / Jacket", "Sweater / T-Shirt", "Shoes")
        val hygieneItems = arrayOf("Hygiene-Kit")

        // Kategori seçildiğinde item'ların güncellenmesi
        binding.autoCompleteCategory.setOnItemClickListener { parent, view, position, id ->
            val selectedCategory = parent.getItemAtPosition(position).toString()
            val adapterItem = when (selectedCategory) {
                "Food" -> ArrayAdapter(this, R.layout.list_item, foodItems)
                "Clothes" -> ArrayAdapter(this, R.layout.list_item, clothesItems)
                "Hygene" -> ArrayAdapter(this, R.layout.list_item, hygieneItems)
                else -> ArrayAdapter(this, R.layout.list_item, arrayOf<String>())
            }
            binding.autoCompleteUrun.setAdapter(adapterItem)
        }

        // Miktar için adapter ayarlama
        val quantity = resources.getStringArray(R.array.quantity)
        val adapterQuantity = ArrayAdapter(this, R.layout.list_item, quantity)
        binding.autoCompleteMiktar.setAdapter(adapterQuantity)

        val submitButton = findViewById<Button>(R.id.autformApproved)
        submitButton.setOnClickListener {
            submitDonation()
        }

        fetchUserDetails()
    }




    private fun submitDonation() {
        val userDetails = getUserDetails()

        db.collection("requesthelp").add(userDetails)
            .addOnSuccessListener {
                Toast.makeText(this, "Donation added successfully", Toast.LENGTH_SHORT).show()
            }
            .addOnFailureListener { e ->
                Log.e("FirebaseError", "Error adding donation: ${e.message}")
                Toast.makeText(this, "Error adding donation: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun getUserDetails(): HashMap<String, Any> { // Changed from String to Any to accommodate Boolean values
        return hashMapOf(
            "autname" to binding.authformname.text.toString(),
            "autsurname" to binding.autformsurname.text.toString(),
            "autphone" to binding.autformphone.text.toString(),
            "autemail" to binding.autformemail.text.toString(),
            "autregion" to binding.autregion.text.toString(),
            "autcocenter" to binding.autaidcenter.text.toString(),
            "autcategory" to binding.autoCompleteCategory.text.toString(),
            "autitem" to binding.autoCompleteUrun.text.toString(),
            "autquantity" to binding.autoCompleteMiktar.text.toString(),
            "isSended" to false // Added new field
        )
    }



    private fun fetchUserDetails() {
        val sessionManager = SessionManager.getInstance(applicationContext)
        val userId = sessionManager.loggedInUserId

        if (userId != null) {
            db.collection("authorized") // Kullanıcı bilgilerinin saklandığı koleksiyon
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        binding.authformname.setText(document.getString("autname"))
                        binding.autformsurname.setText(document.getString("autsurname"))
                        binding.autformphone.setText(document.getString("autphone"))
                        binding.autformemail.setText(document.getString("autemail"))
                        binding.autregion.setText(document.getString("autregion"))
                        binding.autaidcenter.setText(document.getString("autcocenter"))
                    } else {
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show()
                }
        }
    }

}
