package com.example.bridge_authorized

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.widget.Button
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.bridge_authorized.databinding.ActivityCardBinding
import com.example.bridge_authorized.databinding.ActivityDonateSectionBinding
import com.example.bridge_authorized.databinding.ActivityEachItemFoodBinding

class card_activity : AppCompatActivity() {
    private lateinit var binding: ActivityCardBinding
    private lateinit var recyclerView: RecyclerView
    private var mListCard: ArrayList<DonationTypeCard> = ArrayList()
    private lateinit var sharedPreferences: SharedPreferences
    private lateinit var adapter: DonationTypeCardAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityCardBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val btnApprove = findViewById<Button>(R.id.btnApprove)
        btnApprove.setOnClickListener {
            val intent = Intent(this, donor_form::class.java)

            // Sepet verilerini ekleyin
            intent.putExtra("donationData", mListCard) // mListCard, ArrayList<DonationTypeCard> tipindedir

            startActivity(intent)
        }


        recyclerView = findViewById(R.id.recyclerViewDonateCardType)
        recyclerView.setHasFixedSize(true)
        recyclerView.layoutManager = LinearLayoutManager(this)

        // Intent'ten verileri al FOOD Alanı için
        sharedPreferences = getSharedPreferences("com.example.bridge_authorized", Context.MODE_PRIVATE)

        val img = sharedPreferences.getInt("IMG", -1)
        val item = sharedPreferences.getString("ITEM", "")
        val quantity = sharedPreferences.getString("QUANTITY", "")

        val imgkonserve = sharedPreferences.getInt("IMGkonserve", -1)
        val itemkonservebos = sharedPreferences.getString("ITEMkonserve", "")
        val quantitykonserve = sharedPreferences.getString("QUANTITYkonserve", "")

        val imgkurugıda = sharedPreferences.getInt("IMGkurugıda", -1)
        val itemkurugıdabos = sharedPreferences.getString("ITEMkurugıda", "")
        val quantitykurugıda = sharedPreferences.getString("QUANTITYkurugıda", "")


        // Clothes alanı için ekleme
        val imgMont = sharedPreferences.getInt("IMGMont", -1)
        val itemMont = sharedPreferences.getString("ITEMMont", "")
        val quantityMont = sharedPreferences.getString("QUANTITYMont", "")

        val imgSweater = sharedPreferences.getInt("IMGSweater", -1)
        val itemSweater = sharedPreferences.getString("ITEMSweater", "")
        val quantitySweater = sharedPreferences.getString("QUANTITYSweater", "")

        val imgShoes = sharedPreferences.getInt("IMGShoes", -1)
        val itemShoes = sharedPreferences.getString("ITEMShoes", "")
        val quantityShoes = sharedPreferences.getString("QUANTITYShoes", "")


        mListCard = ArrayList()
        //Food Alanı için ekleme
        if (item != null) {
            if (quantity != null) {
                addDataToList(img, "Food", item, "Quantity:", quantity)
            }
        }
        if (itemkonservebos != null) {
            if (quantitykonserve != null) {
                addDataToList(imgkonserve, "Food", itemkonservebos, "Quantity:", quantitykonserve)
            }
        }

        if (itemkurugıdabos != null) {
            if (quantitykurugıda != null) {
                addDataToList(imgkurugıda, "Food", itemkurugıdabos, "Quantity:", quantitykurugıda)
            }
        }

        //Clothes Alanı için ekleme

        if (itemMont != null) {
            if (quantityMont != null) {
                addDataToList(imgMont, "Clothes", itemMont, "Quantity:", quantityMont)
            }
        }
        if (itemSweater != null) {
            if (quantitySweater != null) {
                addDataToList(imgSweater, "Clothes", itemSweater, "Quantity:", quantitySweater)
            }
        }
        if (itemShoes != null) {
            if (quantityShoes != null) {
                addDataToList(imgShoes, "Clothes", itemShoes, "Quantity:", quantityShoes)
            }
        }

        // Adapter'i ayarla ve öğe silme işlevi ekle
        adapter = DonationTypeCardAdapter(mListCard, { position ->
            // Liste içindeki öğeyi sil
        }, { position ->
            // SharedPreferences içindeki öğeyi sil
            deleteItemFromSharedPreferences(position)
        })
        recyclerView.adapter = adapter

    }

    fun deleteItemFromSharedPreferences(position: Int) {
        val currentItem = mListCard[position]
        val category = currentItem.txtCategory

        if (category == "Food") {
            // Food kategorisi için anahtarlar
            sharedPreferences.edit()
                .remove("IMG")
                .remove("ITEM")
                .remove("QUANTITY")

                .remove("IMGkonserve")
                .remove("ITEMkonserve")
                .remove("QUANTITYkonserve")

                .remove("IMGkurugıda")
                .remove("ITEMkurugıda")
                .remove("QUANTITYkurugıda")
                .apply()
            mListCard.removeAt(position)
            adapter.notifyItemRemoved(position)
            adapter.notifyDataSetChanged()
        }
        else if (category == "Clothes") {
            // Food kategorisi için anahtarlar
            sharedPreferences.edit()
                .remove("IMGMont")
                .remove("ITEMMont")
                .remove("QUANTITYMont")

                .remove("IMGSweater")
                .remove("ITEMSweater")
                .remove("QUANTITYSweater")

                .remove("IMGShoes")
                .remove("ITEMShoes")
                .remove("QUANTITYShoes")
                .apply()
            mListCard.removeAt(position)
            adapter.notifyItemRemoved(position)
            adapter.notifyDataSetChanged()
        }



        else {
            // Diğer kategoriler için silme işlemi
            val item = currentItem.txtUrun
            val imgKey = "${category}_${item}_IMG"
            val quantityKey = "${category}_${item}_QUANTITY"

            sharedPreferences.edit().remove(imgKey).remove(quantityKey).apply()
        }

        // mListCard'dan öğeyi kaldır ve adapter'a bildir

    }




    private fun addDataToList(img: Int, category: String, urun: String, adetBos: String, adet: String) {
        if (img != -1) { // Geçerli bir resim ID'si olduğundan emin ol
            mListCard.add(DonationTypeCard(img, category, urun, adetBos, adet))
        }
    }
}
