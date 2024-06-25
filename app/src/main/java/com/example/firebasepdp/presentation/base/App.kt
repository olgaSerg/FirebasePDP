package com.example.firebasepdp.presentation.base

import android.app.Application
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FirebaseFirestore

class App : Application() {

    lateinit var firestore: FirebaseFirestore
        private set

    lateinit var firebaseDatabase: FirebaseDatabase
        private set

    override fun onCreate() {
        super.onCreate()
        firestore = FirebaseFirestore.getInstance()
        firebaseDatabase = FirebaseDatabase.getInstance()
    }

    init {
        instance = this
    }

    companion object {
        private lateinit var instance: App

        fun getAppInstance(): App {
            return instance
        }
    }
}