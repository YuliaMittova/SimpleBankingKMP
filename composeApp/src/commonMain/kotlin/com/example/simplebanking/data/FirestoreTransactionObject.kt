package com.example.simplebanking.data

import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.serialization.Serializable

@Serializable
data class FirestoreTransactionObject(
    val email: String,
    val amount: Double,
    val currency: String,
    val timestamp: Timestamp,
)