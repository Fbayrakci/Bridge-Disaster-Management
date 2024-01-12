package com.example.bridge_authorized

import SeeRequestAdapter
import SessionManager
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.example.bridge_authorized.databinding.ActivitySeedonationsItemBinding
import com.example.bridge_authorized.databinding.ActivitySeerequestItemBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.Query
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class seerequest_item : AppCompatActivity() {

    private lateinit var binding: ActivitySeerequestItemBinding
    private var db = FirebaseFirestore.getInstance()
    private lateinit var navView: BottomNavigationView


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySeerequestItemBinding.inflate(layoutInflater)
        setContentView(binding.root)

        db = Firebase.firestore
        fetchRequests()

        val annoucment = findViewById<ImageView>(R.id.annoucment)
        annoucment.setOnClickListener{
            val intent = Intent(this, aut_add_annoucments::class.java)
            startActivity(intent)
        }

        navView = findViewById(R.id.nav_view)

        // Bu satırı ekleyin
        navView.selectedItemId = R.id.nav3

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
    private fun fetchRequests() {
        db.collection("requesthelp")
            .whereEqualTo("isSended", false) // Sadece isSended false olanları getir
            .get()
            .addOnSuccessListener { documents ->
                val requestsList = documents.mapNotNull { document ->
                    val requestItem = document.toObject(SeeRequestItem::class.java)
                    requestItem.id = document.id // Belgenin ID'sini ekleyin
                    requestItem
                }
                setupRecyclerView(requestsList)
            }
            .addOnFailureListener { exception ->
                Log.e("FirestoreError", "Error getting documents: ", exception)
            }
    }
    // processRequest fonksiyonunu güncelle
    private fun processRequest(request: SeeRequestItem, sessionManager: SessionManager) {
        val loggedInUserId = sessionManager.loggedInUserId

        loggedInUserId?.let { userId ->
            db.collection("authorized")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Kullanıcı bilgilerini al
                        val autcenter = document.getString("autcocenter") ?: ""

                        // Kullanıcının merkezi ile depodaki merkez eşleşiyor mu kontrol et
                        checkInventory(request, autcenter)
                    } else {
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun checkInventory(request: SeeRequestItem, autcenter: String) {
        db.collection("storage")
            .whereEqualTo("center", autcenter)
            .whereEqualTo("category", request.autcategory)
            .whereEqualTo("item", request.autitem)
            .get()
            .addOnSuccessListener { storageDocuments ->
                if (!storageDocuments.isEmpty) {
                    // Check if there is a matching item in the storage for the specified center
                    val matchingItem = storageDocuments.documents.firstOrNull()

                    if (matchingItem != null) {
                        val storageQuantity = matchingItem.getLong("quantity") ?: 0
                        val requestQuantity = request.autquantity.toLong()

                        if (storageQuantity >= requestQuantity) {
                            // There is enough quantity in the storage, process the request
                            updateRequestAndAddDonation(request, autcenter, SessionManager.getInstance(this))
                        } else {
                            Toast.makeText(this, "Insufficient quantity in the storage", Toast.LENGTH_SHORT).show()
                        }
                    } else {
                        Log.d("Firestore", "No matching item found in the storage for the specified center")
                        Toast.makeText(this, "No matching item in the storage", Toast.LENGTH_SHORT).show()
                    }
                } else {
                    Log.d("Firestore", "No matching item found in the storage for the specified center (empty result)")
                    Toast.makeText(this, "No matching item in the storage", Toast.LENGTH_SHORT).show()
                }
            }
            .addOnFailureListener {
                Log.e("FirestoreError", "Error checking storage: $it")
                Toast.makeText(this, "Error checking storage", Toast.LENGTH_SHORT).show()
            }
    }


    //...

    private fun updateRequestAndAddDonation(request: SeeRequestItem, autcenter: String, sessionManager: SessionManager) {
        // Talebi işle ve diğer işlemleri gerçekleştir...
        // isSended değerini true olarak güncelle
        db.collection("requesthelp").document(request.id)
            .update("isSended", true)
            .addOnSuccessListener {
                Toast.makeText(this, "Request marked as sent", Toast.LENGTH_SHORT).show()

                // Remove the processed request from the adapter's data
                val updatedList = adapter.seereqlist.toMutableList()
                val position = updatedList.indexOfFirst { it.id == request.id }
                if (position != -1) {
                    updatedList.removeAt(position)
                    adapter.seereqlist = updatedList.toList()

                    // Notify the adapter about the removal
                    adapter.notifyItemRemoved(position)
                    adapter.notifyItemRangeChanged(position, updatedList.size)

                    // Get user details from the authorized collection
                    fetchAuthorizedUserDetails(request, updatedList, sessionManager)

                    // Depodan talep edilen miktar kadar çıkar
                    subtractQuantityFromStorageAsync(request, autcenter)
                }
            }
            .addOnFailureListener {
                Log.e("FirebaseError", "Error updating isSended: $it")
                Toast.makeText(this, "Error updating isSended: $it", Toast.LENGTH_SHORT).show()
            }
    }

    private fun subtractQuantityFromStorageAsync(request: SeeRequestItem, autcenter: String) {
        db.collection("storage")
            .whereEqualTo("center", autcenter)
            .whereEqualTo("category", request.autcategory)
            .whereEqualTo("item", request.autitem)
            .get()
            .addOnSuccessListener { storageDocuments ->
                if (!storageDocuments.isEmpty) {
                    // Check if there is a matching item in the storage for the specified center
                    val matchingItem = storageDocuments.documents.firstOrNull()

                    if (matchingItem != null) {
                        val currentQuantity = matchingItem.getLong("quantity") ?: 0
                        val requestQuantity = request.autquantity.toLong()

                        if (currentQuantity >= requestQuantity) {
                            // Güncellenmiş miktarı hesapla ve depoya kaydet
                            val updatedQuantity = currentQuantity - requestQuantity

                            matchingItem.reference.update("quantity", updatedQuantity)
                                .addOnSuccessListener {
                                    Log.d("Firestore", "Quantity updated in the storage")

                                    // Depodan gönderilen miktar kadar ürün düşürüldüğüne dair mesaj göster
                                    showQuantityDecreasedMessage(request.autitem, requestQuantity.toInt())
                                }
                                .addOnFailureListener {
                                    Log.e("FirestoreError", "Error updating quantity in the storage: $it")
                                }
                        } else {
                            Log.e("FirestoreError", "Trying to subtract more quantity than available in the storage")
                        }
                    }
                }
            }
    }

    private fun showQuantityDecreasedMessage(item: String, quantity: Int) {
        val message = "Depodan $item gönderdiğiniz kadar ürün düşürüldü: $quantity adet"
        Toast.makeText(this, message, Toast.LENGTH_SHORT).show()
    }




    // fetchAuthorizedUserDetails fonksiyonunu güncelle
    private fun fetchAuthorizedUserDetails(request: SeeRequestItem, updatedList: List<SeeRequestItem>, sessionManager: SessionManager) {
        val loggedInUserId = sessionManager.loggedInUserId

        loggedInUserId?.let { userId ->
            db.collection("authorized")
                .document(userId)
                .get()
                .addOnSuccessListener { document ->
                    if (document != null) {
                        // Kullanıcı bilgilerini al
                        val autname = document.getString("autname") ?: ""
                        val autsurname = document.getString("autsurname") ?: ""
                        val autemail = document.getString("autemail") ?: ""
                        val autphone = document.getString("autphone") ?: ""

                        // Diğer işlemleri gerçekleştir...
                        // Örneğin, bu bilgileri kullanarak yeni bir bağış ekleyebilirsiniz.
                        addDonationToCollection(request, autname, autsurname, autemail, autphone)

                        // Burada diğer işlemleri de gerçekleştirebilirsiniz
                    } else {
                        Toast.makeText(this, "User data not found", Toast.LENGTH_SHORT).show()
                    }
                }
                .addOnFailureListener {
                    Toast.makeText(this, "Error fetching user data", Toast.LENGTH_SHORT).show()
                }
        }
    }

    private fun addDonationToCollection(request: SeeRequestItem, autname: String, autsurname: String, autemail: String, autphone: String) {
        // Yeni bir donationItem oluştur
        val donationItem = hashMapOf(
            "userDetails" to hashMapOf(
                "center" to request.autcocenter,
                "email" to autemail,
                "name" to autname,
                "phone" to autphone,
                "region" to request.autregion,
                "surname" to autsurname
            ),"isreceived" to false,
            "isOrdinary" to false,
            "donationItems" to arrayListOf(
                hashMapOf(
                    "category" to request.autcategory, // autcategory'i uygun şekilde ayarlamalısınız
                    "item" to request.autitem,
                    "quantity" to request.autquantity,
                )
            )
        )

        // donations koleksiyonuna yeni donationItem'ı ekle
        db.collection("donations")
            .add(donationItem)
            .addOnSuccessListener {
                Log.d("Firestore", "Donation added to the collection")
            }
            .addOnFailureListener {
                Log.e("FirestoreError", "Error adding donation to the collection: $it")
            }
    }



    private val adapter: SeeRequestAdapter by lazy {
        SeeRequestAdapter(emptyList()) { request, position ->
            processRequest(request, SessionManager.getInstance(this)) // SessionManager'ın getInstance metodu kullanılarak örnek oluşturuldu
            adapter.updateItem(position)
        }
    }


    private fun setupRecyclerView(requests: List<SeeRequestItem>) {
        adapter.seereqlist = requests
        binding.recyclerViewSeeRequest.layoutManager = LinearLayoutManager(this)
        binding.recyclerViewSeeRequest.adapter = adapter

        Log.d("RecyclerView", "RecyclerView setup with ${requests.size} items")
    }


}