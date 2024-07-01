package com.example.firebasepdp.model

import com.google.firebase.Timestamp

data class Note(
    var id: String = "",
    var title: String = "",
    var content: String = "",
    var date: Timestamp = Timestamp.now(),
    var imageUrl: String = ""
)