package com.daominh.quickmem.data.firebase

import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore

class FirebaseUserManager {
    private val firebaseAuth = FirebaseAuth.getInstance()
    private val firebaseUser = firebaseAuth.currentUser
    private val firebaseFirestore = FirebaseFirestore.getInstance()

    fun FirebaseUserManager(){

    }
}