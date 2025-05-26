package com.example.simplebanking.screens.faker

import com.example.simplebanking.api.FirestoreRepository
import com.example.simplebanking.data.FirestoreTransactionObject
import com.example.simplebanking.data.TransactionListResult

class FakeFirestoreRepository : FirestoreRepository {
    private val transactionList = mutableListOf<FirestoreTransactionObject>()

    override suspend fun createTransaction(transaction: FirestoreTransactionObject) {
        transactionList.add(transaction)
    }

    override suspend fun fetchTransactions(): TransactionListResult {
        return TransactionListResult(transactionList = transactionList)
    }
}