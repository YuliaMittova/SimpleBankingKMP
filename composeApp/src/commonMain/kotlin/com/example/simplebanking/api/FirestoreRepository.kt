package com.example.simplebanking.api

import co.touchlab.kermit.Logger
import com.example.simplebanking.data.FirestoreTransactionObject
import com.example.simplebanking.data.TransactionListResult
import dev.gitlive.firebase.Firebase
import dev.gitlive.firebase.firestore.FirebaseFirestore
import dev.gitlive.firebase.firestore.QuerySnapshot
import dev.gitlive.firebase.firestore.firestore
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.asFlow
import kotlinx.coroutines.flow.flatMapConcat
import kotlinx.coroutines.flow.map

interface FirestoreRepository {

    suspend fun createTransaction(transaction: FirestoreTransactionObject)
    // fetch transactions as list
    suspend fun fetchTransactions(): TransactionListResult
    // fetch transactions as Flow
    fun getSnapshotFlow(): Flow<FirestoreTransactionObject>
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

    @OptIn(ExperimentalCoroutinesApi::class)
    override fun getSnapshotFlow(): Flow<FirestoreTransactionObject> {
        return Firebase.firestore
            .collection(COLLECTION_REF)
            .snapshots
            .flatMapConcat { querySnapshot: QuerySnapshot ->
                querySnapshot.documents.asFlow().map { documentSnapshot ->
                    FirestoreTransactionObject(
                        email = documentSnapshot.get("email"),
                        amount = documentSnapshot.get("amount"),
                        currency = documentSnapshot.get("currency"),
                        timestamp = documentSnapshot.get("timestamp"),
                    )
                }
            }
    }

    private companion object {
        const val TAG = "FirestoreRepository"
        const val COLLECTION_REF = "TRANSACTIONS"
    }
}