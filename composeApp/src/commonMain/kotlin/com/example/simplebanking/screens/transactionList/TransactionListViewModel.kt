package com.example.simplebanking.screens.transactionList

import androidx.annotation.VisibleForTesting
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import co.touchlab.kermit.Logger
import com.example.simplebanking.api.FirestoreRepository
import com.example.simplebanking.domain.Currency
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Date

class TransactionListViewModel(
    private val firestoreRepository: FirestoreRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionListUIState())
    val uiState: StateFlow<TransactionListUIState> = _uiState.asStateFlow()

    init {
        loadTransactions()
    }

    @VisibleForTesting
    internal fun loadTransactions() {
        _uiState.update {
            it.copy(status = TransactionListStatus.LOADING)
        }

        viewModelScope.launch {
            val transactionData = firestoreRepository.fetchTransactions()
            _uiState.update { uiState ->
                if (!transactionData.errorMessage.isNullOrEmpty()) {
                    Logger.withTag(TAG).e(transactionData.errorMessage)
                    uiState.copy(
                        status = TransactionListStatus.ERROR,
                        errorMessage = transactionData.errorMessage
                    )
                } else {
                    uiState.copy(
                        status = TransactionListStatus.SUCCESS,
                        transactionData = transactionData.transactionList.map {
                            TransactionData(
                                email = it.email,
                                amount = it.amount,
                                currency = Currency.valueOf(it.currency),
                                timestamp = DATE_FORMATTER.format(Date(it.timestamp.seconds * 1000))
                            )
                        })
                }
            }
        }
    }

    private companion object {
        const val TAG = "TransactionListViewModel"
        val DATE_FORMATTER: SimpleDateFormat = SimpleDateFormat("dd/MM HH:mm")
    }
}
