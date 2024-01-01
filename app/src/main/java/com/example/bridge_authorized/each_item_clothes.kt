package com.example.bridge_authorized

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.EditText
import android.widget.TextView
import android.widget.Toast

class each_item_clothes : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_each_item_clothes)
    }

    @SuppressLint("WrongViewCast")
    fun onAddToCartClicked(view: View) {
        val txtSectionMont = findViewById<TextView>(R.id.txtSectionMont)
        val txtSetQuantityMont = findViewById<EditText>(R.id.txtSetQuantityMont)
        val imgMont = R.drawable.coat
        val itemMont = txtSectionMont.text.toString()
        val quantityMont = txtSetQuantityMont.text.toString()

        val txtSectionSweater = findViewById<TextView>(R.id.txtSectionSweater)
        val txtSetQuantitySweater = findViewById<EditText>(R.id.txtSetQuantitySweater)
        val imgSweater = R.drawable.kazak1
        val itemSweater = txtSectionSweater.text.toString()
        val quantitySweater = txtSetQuantitySweater.text.toString()

        val txtSectionShoes = findViewById<TextView>(R.id.txtSectionAyakkabı)
        val txtSectionQuanShoes = findViewById<EditText>(R.id.txtSetQuantityAyakkabıMiktar)
        val imgShoes = R.drawable.shoes
        val itemShoes = txtSectionShoes.text.toString()
        val quantityShoes = txtSectionQuanShoes.text.toString()


        val sharedPreferences = getSharedPreferences("com.example.bridge_authorized", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        var validQuantityFound = false


        // MOnt kontrol
        if (!quantityMont.isEmpty() && quantityMont != "0") {
            editor.putInt("IMGMont", imgMont)
            editor.putString("ITEMMont", itemMont)
            editor.putString("QUANTITYMont", quantityMont)
            validQuantityFound = true
        }

        // Sweater için kontrol
        if (!quantitySweater.isEmpty() && quantitySweater != "0") {
            editor.putInt("IMGSweater", imgSweater)
            editor.putString("ITEMSweater", itemSweater)
            editor.putString("QUANTITYSweater", quantitySweater)
            validQuantityFound = true
        }

        // Shoes kontrol
        if (!quantityShoes.isEmpty() && quantityShoes != "0") {
            editor.putInt("IMGShoes", imgShoes)
            editor.putString("ITEMShoes", itemShoes)
            editor.putString("QUANTITYShoes", quantityShoes)
            validQuantityFound = true
        }

        if (!validQuantityFound) {
            Toast.makeText(
                this,
                "Please enter a valid quantity (more than 0) for at least one item",
                Toast.LENGTH_SHORT
            ).show()
            return
        }

        editor.apply()

        startActivity(Intent(this, card_activity::class.java))
    }
}