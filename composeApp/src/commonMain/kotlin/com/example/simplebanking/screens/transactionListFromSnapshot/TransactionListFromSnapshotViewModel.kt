package com.example.simplebanking.screens.transactionListFromSnapshot

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import com.example.simplebanking.api.FirestoreRepository
import com.example.simplebanking.domain.Currency
import com.example.simplebanking.screens.transactionList.TransactionData
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map
import java.text.SimpleDateFormat
import java.util.Date

class TransactionListFromSnapshotViewModel(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    @VisibleForTesting
    internal fun fetchTransactionsFromSnapshot(): Flow<TransactionData> {
        return firestoreRepository.getSnapshotFlow()
            .map {
                TransactionData(
                    email = it.email,
                    amount = it.amount,
                    currency = Currency.valueOf(it.currency),
                    timestamp = DATE_FORMATTER.format(Date(it.timestamp.seconds * 1000))
                )
            }
    }

    private companion object {
        val DATE_FORMATTER: SimpleDateFormat = SimpleDateFormat("dd/MM HH:mm")
    }
}
