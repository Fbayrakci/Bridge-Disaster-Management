package com.example.bridge_authorized

data class SeeDonationType(
    var id: String = "", // Bu, Firestore belgesinin kimliğini temsil eder
    val userDetails: HashMap<String, String> = hashMapOf(),
    val donationItems: List<HashMap<String, String>> = listOf(),
    val isreceived: Boolean = false,
    val isauthorized: Boolean= false,
    var isOrdinary: Boolean = false,

    )