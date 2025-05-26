package com.example.simplebanking.screens.transactionList

import com.example.simplebanking.domain.Currency
import com.google.type.DateTime

data class TransactionListUIState(
    val status: TransactionListStatus = TransactionListStatus.LOADING,
    val transactionData: List<TransactionData> = emptyList(),
    val errorMessage: String? = null
)

data class TransactionData(
    val email: String,
    val amount: Double,
    val currency: Currency,
    val timestamp: String,
)

enum class TransactionListStatus {
    LOADING, ERROR, SUCCESS
}