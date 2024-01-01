package com.example.bridge_authorized

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.util.Log
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity

class depo_activity : AppCompatActivity() {

    private lateinit var quantityReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_depo)

        // BroadcastReceiver tanımlama
        quantityReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                val quantity = intent.getStringExtra("quantity")
                runOnUiThread {
                    findViewById<TextView>(R.id.txtSectionSuMiktar)?.text = quantity
                }
            }
        }


        // Intent filtresi ile BroadcastReceiver'ı kaydetme
        val filter = IntentFilter("com.example.bridge_authorized.UPDATE_QUANTITY")
        registerReceiver(quantityReceiver, filter)
    }

    override fun onDestroy() {
        super.onDestroy()
        // BroadcastReceiver'ın kaydını iptal etme
        unregisterReceiver(quantityReceiver)
    }
}
