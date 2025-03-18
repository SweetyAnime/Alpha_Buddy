package com.nk.alphabuddy

data class Event(
    val name: String,
    val imageURL: String,
    val date: String,
    val remainingTime: String // Change from Int to String
)
