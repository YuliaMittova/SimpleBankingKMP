package com.example.simplebanking.api

import co.touchlab.kermit.Logger
import com.example.simplebanking.data.FirestoreTransactionObject
import com.example.simplebanking.data.TransactionListResult
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.firestore

interface FirestoreRepository {

    suspend fun createTransaction(transaction: FirestoreTransactionObject)
    suspend fun fetchTransactions(): TransactionListResult
}

class FirestoreRepositoryImpl : FirestoreRepository {
    private var firebaseFirestore: FirebaseFirestore = Firebase.firestore

    override suspend fun createTransaction(transaction: FirestoreTransactionObject) {
        try {
            Firebase.firestore
                .collection(COLLECTION_REF)
                .document
                .set(transaction)
        } catch (e: Exception) {
            Logger.withTag(TAG)
                .e { "Error occurred while adding transaction with email ${transaction.email}" }
            throw e
        }
    }

    override suspend fun fetchTransactions(): TransactionListResult {
        try {
            val userResponse = firebaseFirestore.collection(COLLECTION_REF).get()
            return TransactionListResult(transactionList = userResponse.documents.map { it.data() })
        } catch (e: Exception) {
            val errorMessage = e.message ?: "Error occurred while fetching transactions"
            Logger.withTag(TAG).e { errorMessage }
            return TransactionListResult(errorMessage = errorMessage)
        }
    }

    private companion object {
        const val TAG = "FirestoreRepository"
        const val COLLECTION_REF = "TRANSACTIONS"
    }
}