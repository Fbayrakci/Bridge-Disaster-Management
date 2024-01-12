package com.example.bridge_authorized

data class AuthorizedPerson(
    var id: String = "",
    val autname: String = "",
    val autsurname: String = "",
    val autemail: String = ""
    // Ensure all properties have default values
)
