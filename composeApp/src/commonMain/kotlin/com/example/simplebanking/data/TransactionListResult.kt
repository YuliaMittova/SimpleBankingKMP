package com.example.simplebanking.data

data class TransactionListResult (
    val transactionList: List<FirestoreTransactionObject> = emptyList(),
    val errorMessage: String? = null
)