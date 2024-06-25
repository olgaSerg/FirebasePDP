package com.example.firebasepdp.model

import java.time.LocalDate

data class Note(
    var id: String = "",
    var title: String = "",
    var content: String = "",
    var date: LocalDate = LocalDate.now()
)