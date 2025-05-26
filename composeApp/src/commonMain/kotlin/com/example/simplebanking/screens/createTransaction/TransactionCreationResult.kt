package com.example.simplebanking.screens.createTransaction

data class TransactionCreationUIState(
    val status: TransactionCreationStatus = TransactionCreationStatus.IDLE,
    val message: String? = null
)

enum class TransactionCreationStatus {
    IDLE, TRANSACTION_INITIATED, ERROR, SUCCESS
}