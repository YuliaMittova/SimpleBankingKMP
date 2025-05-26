package com.example.simplebanking.data

import kotlinx.serialization.Serializable

@Serializable
data class CreateTransactionRequest(
    @Serializable
    val recipientEmail: String,
    @Serializable
    val amount: Double,
    @Serializable
    val currency: String,
)