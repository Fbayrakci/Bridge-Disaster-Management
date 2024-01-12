package com.example.bridge_authorized

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast

class each_item_food : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_each_item_food)

        val back = findViewById<ImageView>(R.id.back)

        back.setOnClickListener {
            val intent = Intent(this, donate_section::class.java)
            startActivity(intent)
            finish()
        }
    }

    @SuppressLint("WrongViewCast")
    fun onAddToCartClicked(view: View) {


        val txtSectionSu = findViewById<TextView>(R.id.txtSectionSu)
        val txtSetQuantitySu = findViewById<EditText>(R.id.txtSetQuantitySu)
        val img = R.drawable.susisesi
        val item = txtSectionSu.text.toString()
        val quantity = txtSetQuantitySu.text.toString()

        val txtSectionKonserve = findViewById<TextView>(R.id.txtSectionKonserve)
        val txtSectionQuanKonserve = findViewById<EditText>(R.id.txtSetQuantityKonserve)
        val imgkonserve = R.drawable.konserve
        val itemkonserve = txtSectionKonserve.text.toString()
        val quantitykonserve = txtSectionQuanKonserve.text.toString()

        val txtSectionKurugıda = findViewById<TextView>(R.id.txtSectionKuruGıda)
        val txtSectionQuanKurugıda = findViewById<EditText>(R.id.txtSetQuantityKuruGıdaMiktar)
        val imgkurugıda = R.drawable.legumes
        val itemkurugıda = txtSectionKurugıda.text.toString()
        val quantitykurugıda = txtSectionQuanKurugıda.text.toString()


        val sharedPreferences = getSharedPreferences("com.example.bridge_authorized", Context.MODE_PRIVATE)
        val editor = sharedPreferences.edit()

        var validQuantityFound = false

        if (quantity.isNotEmpty() && quantity != "0") {
            editor.putInt("IMG", img)
            editor.putString("ITEM", item)
            editor.putString("QUANTITY", quantity)
            validQuantityFound = true
        }

        if (quantitykonserve.isNotEmpty() && quantitykonserve != "0") {
            editor.putInt("IMGkonserve", imgkonserve)
            editor.putString("ITEMkonserve", itemkonserve)
            editor.putString("QUANTITYkonserve", quantitykonserve)
            validQuantityFound = true
        }

        if (quantitykurugıda.isNotEmpty() && quantitykurugıda != "0") {
            editor.putInt("IMGkurugıda", imgkurugıda)
            editor.putString("ITEMkurugıda", itemkurugıda)
            editor.putString("QUANTITYkurugıda", quantitykurugıda)
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