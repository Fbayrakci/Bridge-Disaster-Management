package com.example.bridge_authorized
data class SeeRequestItem(
    var id: String = "",
    val autname: String = "",
    val autsurname: String = "",
    val autphone: String = "",
    val autemail: String = "",
    val autcategory: String = "",
    val autitem: String = "",
    val autquantity: String = "",
    val autregion: String = "",
    val autcocenter: String = "",  // Yeni eklenen özellik
    val isSended: Boolean = false
)