package com.example.simplebanking.data

import kotlinx.serialization.Serializable

@Serializable
data class FirestoreTransactionObject(
    val email: String,
    val amount: Double,
    val currency: String,
)