package com.example.bridge_authorized

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.ArrayAdapter
import android.widget.AutoCompleteTextView
import android.widget.Button
import android.widget.Toast
import com.example.bridge_authorized.databinding.ActivityDonorFormBinding
import com.example.bridge_authorized.databinding.ActivitySignUpAuthorizedBinding
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class donor_form : AppCompatActivity() {
    private lateinit var binding: ActivityDonorFormBinding
    private var db = Firebase.firestore
    private lateinit var sharedPreferences: SharedPreferences  // Declare at the class level


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityDonorFormBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val donationData = intent.getSerializableExtra("donationData") as? ArrayList<DonationTypeCard>
        processDonationData(donationData)

        val submitButton = findViewById<Button>(R.id.donorformApproved)
        submitButton.setOnClickListener {
            if (donationData != null) {
                submitDonation(donationData)
                val intent = Intent(this, donor_dashboard::class.java)
                startActivity(intent)

            } else {
                Toast.makeText(this, "No donation data to submit", Toast.LENGTH_SHORT).show()
            }
        }

        fetchUserDetails()
        fetchRegionsAndCenters()
    }

    private fun processDonationData(donationData: ArrayList<DonationTypeCard>?) {
        // Burada donationData listesi üzerinde işlemler yapabilirsiniz
        // Örnek: Her bir öğe için log yazdırma
        donationData?.forEach { donation ->
            Log.d("DonationItem", "Item: ${donation.txtUrun}, Quantity: ${donation.txtAdet}")
        }
    }

    private fun submitDonation(donationData: ArrayList<DonationTypeCard>) {
        val userDetails = getUserDetails()
        val newDonation = hashMapOf(
            "userDetails" to userDetails,
            "donationItems" to donationData.map { donation ->
                hashMapOf(
                    "category" to donation.txtCategory,
                    "item" to donation.txtUrun,
                    "quantity" to donation.txtAdet
                )
            },
            "isreceived" to false, // Adding the new Boolean field
            "isOrdinary" to true // Adding the new Boolean field
        )

        db.collection("donations").add(newDonation)
            .addOnSuccessListener {
                Toast.makeText(this, "Donation added successfully", Toast.LENGTH_SHORT).show()
                donationData.clear()
                clearSharedPreferences()
                val intent = Intent(this@donor_form, donor_dashboard::class.java)
                startActivity(intent)
                finish()
            }
            .addOnFailureListener { e ->
                Toast.makeText(this, "Error adding donation: ${e.message}", Toast.LENGTH_SHORT).show()
            }
    }


    private fun getUserDetails(): HashMap<String, String> {
        return hashMapOf(
            "name" to binding.donorformname.text.toString(),
            "surname" to binding.donorformsurname.text.toString(),
            "phone" to binding.donorformphone.text.toString(),
            "email" to binding.donorformemail.text.toString(),
            "region" to binding.autoCompleteRegion.text.toString(),
            "center" to binding.autoCompleteCoordinationCenter.text.toString()
        )
    }
    private fun clearSharedPreferences() {
        // Get the SharedPreferences Editor
        val sharedPreferences = this.getSharedPreferences("com.example.bridge_authorized", Context.MODE_PRIVATE)
        sharedPreferences.edit().clear().apply()
    }

    private fun fetchUserDetails() {
        val sessionManager = SessionManager.getInstance(applicationContext)
        val userId = sessionManager.loggedInUserId

        if (userId != null) {
            db.collection("donor") // Kullanıcı bilgilerinin saklandığı koleksiyon
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        binding.donorformname.setText(document.getString("name"))
                        binding.donorformsurname.setText(document.getString("surname"))
                        binding.donorformphone.setText(document.getString("phone"))
                        binding.donorformemail.setText(document.getString("email"))
                        // Diğer alanları burada doldurabilirsiniz
                    } else {
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun fetchRegionsAndCenters() {
        db.collection("authorized") // Veritabanınızdaki ilgili koleksiyon adı
            .get()
            .addOnSuccessListener { documents ->
                val regions = mutableListOf<String>()
                val centers = mutableMapOf<String, List<String>>()

                for (document in documents) {
                    val region = document.getString("autregion") ?: ""
                    val center = document.getString("autcocenter") ?: ""

                    if (region.isNotEmpty()) {
                        regions.add(region)
                        centers[region] = centers.getOrDefault(region, mutableListOf()) + center
                    }
                }

                setupAutoCompleteTextView(regions.distinct(), centers)
            }
            .addOnFailureListener {
                Toast.makeText(this, "Failed to fetch data", Toast.LENGTH_LONG).show()
            }
    }

    private fun setupAutoCompleteTextView(regions: List<String>, centers: Map<String, List<String>>) {
        val regionAdapter = ArrayAdapter(this, R.layout.list_item, regions)
        binding.autoCompleteRegion.setAdapter(regionAdapter)

        binding.autoCompleteRegion.setOnItemClickListener { adapterView, _, position, _ ->
            val selectedRegion = adapterView.getItemAtPosition(position).toString()
            val centerAdapter = ArrayAdapter(this, R.layout.list_item, centers[selectedRegion] ?: listOf())
            binding.autoCompleteCoordinationCenter.setAdapter(centerAdapter)
        }
    }
}
