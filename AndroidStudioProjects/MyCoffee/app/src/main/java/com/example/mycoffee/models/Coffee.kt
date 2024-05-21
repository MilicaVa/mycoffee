package com.example.myDonuts.models

data class Donuts(
    val id: String? = null,
    val name: String? = null,
    val price: String? = null,
    val imgUri: String? = null,
    var isSelected: Boolean = false
)

