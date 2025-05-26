package com.example.simplebanking.screens.faker

import com.example.simplebanking.api.FirestoreRepository
import com.example.simplebanking.data.FirestoreTransactionObject
import com.example.simplebanking.data.TransactionListResult
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow

class FakeFirestoreRepository : FirestoreRepository {
    private val transactionList = mutableListOf<FirestoreTransactionObject>()

    override suspend fun createTransaction(transaction: FirestoreTransactionObject) {
        transactionList.add(transaction)
    }

    override suspend fun fetchTransactions(): TransactionListResult {
        return TransactionListResult(transactionList = transactionList)
    }

    override fun getSnapshotFlow(): Flow<FirestoreTransactionObject> {
        return transactionList.asFlow()
    }
}