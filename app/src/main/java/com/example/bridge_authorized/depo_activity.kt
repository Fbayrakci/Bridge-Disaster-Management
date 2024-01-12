package com.example.bridge_authorized

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import android.widget.Toast
import com.google.firebase.firestore.FirebaseFirestore

class depo_activity : AppCompatActivity() {
    private lateinit var db: FirebaseFirestore

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_depo)

        fetchLoggedInUserDetails { userDetails ->
            if (userDetails != null) {
                val userCenter = userDetails["autcocenter"] as? String ?: ""
                fetchDepo(userCenter)
            } else {
                // Kullanıcı bilgisi alınamadığında yapılacak işlemleri burada gerçekleştirebilirsiniz.
            }
        }
        // Firebase Firestore bağlantısı
        db = FirebaseFirestore.getInstance()
        // Veri çekme ve UI güncelleme
    }

    private fun fetchLoggedInUserDetails(callback: (userDetails: Map<String, Any>?) -> Unit) {
        val sessionManager = SessionManager.getInstance(applicationContext)
        val loggedInUserId = sessionManager.loggedInUserId
        if (loggedInUserId != null) {
            val db = FirebaseFirestore.getInstance()
            db.collection("authorized")
                .document(loggedInUserId)
                .get()
                .addOnSuccessListener { documentSnapshot ->
                    val userDetails = documentSnapshot?.data
                    callback(userDetails)
                }
                .addOnFailureListener { exception ->
                    // Hata durumunda işlemleri burada gerçekleştirebilirsiniz.
                    callback(null)
                }
        } else {
            callback(null)
        }
    }
    private fun fetchDepo(userCenter: String) {
        db.collection("storage")
            .whereEqualTo("center", userCenter) // 'center' alanına göre filtreleme
            .get()
            .addOnSuccessListener { documents ->
                for (document in documents) {
                    val category = document.getString("category")
                    val item = document.getString("item")
                    val quantityLong = document.getLong("quantity")
                    val quantity = quantityLong?.toInt().toString()
                    // UI güncellemesi
                    updateUI(category, item, quantity)
                }
            }
            .addOnFailureListener { exception ->
                // Hata durumunda yapılacak işlemler
                Toast.makeText(this, "Error fetching storage data: ${exception.message}", Toast.LENGTH_SHORT).show()
            }
    }

    private fun updateUI(category: String?, item: String?, quantity: String?) {
        if (category == "Food" && item == "Water(12 pieces)") {
            findViewById<TextView>(R.id.txtSectionSuMiktar).text = quantity
        }else if (category == "Food" && item == "Canned Food(1 packet)"){
            findViewById<TextView>(R.id.txtSectionAdetKonserve).text = quantity
        }else if (category == "Food" && item == "Legumes"){
            findViewById<TextView>(R.id.txtSectionAdetKurugıda).text = quantity
        }else if (category == "Clothes" && item == "Coat / Jacket") {
            findViewById<TextView>(R.id.txtSectionAdetMont).text = quantity
        }else if (category == "Clothes" && item == "Sweater / T-Shirt") {
            findViewById<TextView>(R.id.txtSectionAdetSweater).text = quantity
        }else if (category == "Clothes" && item == "Shoes") {
            findViewById<TextView>(R.id.txtSectionAdetShoes).text = quantity
        }
        // Diğer kategori ve öğeler için benzer güncellemeler
    }
}