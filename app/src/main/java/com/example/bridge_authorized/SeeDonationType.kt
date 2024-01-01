package com.example.bridge_authorized

data class SeeDonationType(
    val userDetails: HashMap<String, String> = hashMapOf(),
    val donationItems: List<HashMap<String, String>> = listOf()
)

