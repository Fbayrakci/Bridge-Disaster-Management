package com.example.bridge_authorized

import SeeDonationTypeAdapter
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bridge_authorized.databinding.ActivitySeedonationsItemBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.DocumentSnapshot
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class seedonations_item : AppCompatActivity() {

    private lateinit var binding: ActivitySeedonationsItemBinding
    private var db = FirebaseFirestore.getInstance()
    private lateinit var navView: BottomNavigationView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeedonationsItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.d("seedonations_item", "onCreate called")
        db = FirebaseFirestore.getInstance()
        fetchUserDetailsAndDonations()

        val annoucment = findViewById<ImageView>(R.id.annoucment)
        annoucment.setOnClickListener{
            val intent = Intent(this, aut_add_annoucments::class.java)
            startActivity(intent)
        }

        navView = findViewById(R.id.nav_view)

        // Bu satırı ekleyin
        navView.selectedItemId = R.id.nav2

        navView.setOnItemSelectedListener {
            when (it.itemId) {
                R.id.nav1 -> navigateToActivity(authorized_dashboard::class.java)
                R.id.nav2 -> navigateToActivity(seedonations_item::class.java)
                R.id.nav3 -> navigateToActivity(seerequest_item::class.java)
                R.id.nav4 -> navigateToActivity(aut_profile::class.java)
            }
            true
        }
    }
    private fun navigateToActivity(targetActivity: Class<*>) {
        val intent = Intent(this, targetActivity)
        startActivity(intent)
    }
    private fun fetchUserDetailsAndDonations() {
        Log.d("seedonations_item", "fetchUserDetailsAndDonations called")

        val sessionManager = SessionManager.getInstance(applicationContext)
        val userId = sessionManager.loggedInUserId

        Log.d("seedonations_item", "User ID: $userId")

        if (sessionManager.isLoggedIn && userId != null) {
            db.collection("authorized").document(userId).get()
                .addOnSuccessListener { document ->
                    Log.d("seedonations_item", "Document fetched successfully")

                    val userRegion = document.getString("autregion")
                    val userCenter = document.getString("autcocenter")

                    Log.d("seedonations_item", "User region: $userRegion, User center: $userCenter")

                    if (userRegion != null && userCenter != null) {
                        // Fetch donations only if user details are available
                        fetchDonations(userRegion, userCenter)
                    } else {
                        Log.d("seedonations_item", "User region or center not set")
                        Toast.makeText(this, "Region or Center not set for the user", Toast.LENGTH_LONG).show()
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("seedonations_item", "Error fetching user data: $e")
                    Toast.makeText(this, "Error fetching user data: $e", Toast.LENGTH_SHORT).show()
                }
        } else {
            Log.d("seedonations_item", "No signed-in user")
            Toast.makeText(this, "No signed-in user", Toast.LENGTH_SHORT).show()
        }
    }

    private fun fetchDonations(region: String, center: String) {
        Log.d("seedonations_item", "Fetching donations for Region: $region, Center: $center")

        db.collection("donations")
            .addSnapshotListener { snapshot, exception ->
                Log.d("seedonations_item", "Snapshot: $snapshot")
                Log.d("seedonations_item", "Exception: $exception")

                if (exception != null) {
                    Log.e("seedonations_item", "Error fetching donations: ${exception.localizedMessage}")
                    Toast.makeText(this, exception.localizedMessage, Toast.LENGTH_LONG).show()
                } else {
                    if (snapshot != null) {
                        Log.d("seedonations_item", "Number of documents: ${snapshot.size()}")

                        val matchingDonations = mutableListOf<SeeDonationType>()

                        snapshot.documents.forEach { document ->
                            Log.d("seedonations_item", "Document ID: ${document.id}")
                            // Belge içeriğini daha ayrıntılı logla
                            document.data?.forEach { (key, value) ->
                                Log.d("seedonations_item", "Field: $key, Value: $value")
                            }

                            // Fetch the isOrdinary field along with other fields
                            val isOrdinary = document.getBoolean("isOrdinary") ?: false

                            // processDonations fonksiyonunu çağır
                            val matching = processDonations(document, region, center, isOrdinary)

                            // Eşleşen bağışları listeye ekle
                            matchingDonations.addAll(matching)
                        }

                        // RecyclerView'ı güncelle
                        setupRecyclerView(matchingDonations)
                    } else {
                        Log.d("seedonations_item", "Snapshot is null")
                    }
                }
            }
    }

    // Modify the processDonations function to accept isOrdinary parameter
    private fun processDonations(document: DocumentSnapshot, region: String, center: String, isOrdinary: Boolean): List<SeeDonationType> {
        val matchingDonations = mutableListOf<SeeDonationType>()

        // Donation bilgisini kullanıcı bilgileriyle karşılaştır
        val isreceived = document.getBoolean("isreceived") ?: false
        val isOrdinary = document.getBoolean("isOrdinary") ?: false

        if (isreceived) {
            // Eğer bağış zaten alındıysa, işlem yapma
            return matchingDonations
        }

        // Diğer işlemleri devam ettir
        val donationType = document.toObject(SeeDonationType::class.java)
        if (donationType != null) {
            donationType.id = document.id
            donationType.isOrdinary = isOrdinary
            val donationRegion = donationType.userDetails["region"]
            val donationCenter = donationType.userDetails["center"]

            if (donationRegion == region && donationCenter == center) {
                // Eğer bağış eşleşiyorsa, bu bağışı listeye ekle
                donationType.isOrdinary = isOrdinary
                matchingDonations.add(donationType)
            }
        }
        return matchingDonations
    }



    private fun saveDonationToStorage(donation: SeeDonationType, container: ViewGroup, aggregatedQuantities: Map<Pair<String, String>, Int>) {
        val db = FirebaseFirestore.getInstance()
        val storageRef = db.collection("storage")

        // Bağışın isreceived değerini güncelle
        db.collection("donations").document(donation.id)
            .update("isreceived", true)
            .addOnSuccessListener {
                Log.d("seedonations_item", "isreceived updated successfully")
            }
            .addOnFailureListener {
                Log.e("seedonations_item", "Error updating isreceived: $it")
            }

        // Bağışın ayrıntıları
        val center = donation.userDetails["center"] ?: "Default Center"

        // Toplanmış miktarları işle
        aggregatedQuantities.forEach { (key, newQuantity) ->
            val (category, item) = key

            // Firestore'da mevcut kaydı ara
            storageRef.whereEqualTo("category", category)
                .whereEqualTo("item", item)
                .whereEqualTo("center", center)
                .get()
                .addOnSuccessListener { documents ->
                    if (documents.size() > 0) {
                        // Mevcut kaydı güncelle
                        val document = documents.documents[0]
                        val currentQuantity = document.getLong("quantity")?.toInt() ?: 0
                        val updatedQuantity = currentQuantity + newQuantity

                        storageRef.document(document.id).update("quantity", updatedQuantity)
                            .addOnSuccessListener {
                                Log.d("seedonations_item", "Donation updated successfully")
                                Toast.makeText(container.context, "Donation updated successfully", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Log.e("seedonations_item", "Error updating donation: $it")
                                Toast.makeText(container.context, "Error updating donation", Toast.LENGTH_SHORT).show()
                            }
                    } else {
                        // Yeni kayıt ekle
                        val data = hashMapOf(
                            "category" to category,
                            "item" to item,
                            "quantity" to newQuantity,
                            "center" to center
                        )

                        storageRef.add(data)
                            .addOnSuccessListener {
                                Log.d("seedonations_item", "Successfully Received")
                                Toast.makeText(container.context, "Successfully Received", Toast.LENGTH_SHORT).show()
                            }
                            .addOnFailureListener {
                                Log.e("seedonations_item", "Error Received: $it")
                                Toast.makeText(container.context, "Error Received", Toast.LENGTH_SHORT).show()
                            }
                    }
                }
                .addOnFailureListener { e ->
                    Log.e("seedonations_item", "Error querying storage: $e")
                    Toast.makeText(container.context, "Error querying storage: $e", Toast.LENGTH_SHORT).show()
                }
        }
    }




    // seedonations_item sınıfı içinde
    // Modify the setupRecyclerView function to include onDonationButtonClicked parameter
    private fun setupRecyclerView(donations: List<SeeDonationType>) {
        val adapter = SeeDonationTypeAdapter(donations) { donation, container, aggregatedQuantities ->
            saveDonationToStorage(donation, container, aggregatedQuantities)
        }
        binding.recyclerViewSeeDonations.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSeeDonations.adapter = adapter
    }



}