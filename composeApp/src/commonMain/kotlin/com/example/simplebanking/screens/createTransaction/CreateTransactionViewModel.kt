package com.example.simplebanking.screens.createTransaction

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.simplebanking.api.PaymentsApi
import com.example.simplebanking.api.FirestoreRepository
import com.example.simplebanking.data.FirestoreTransactionObject
import com.example.simplebanking.screens.createTransaction.TransactionCreationStatus.ERROR
import com.example.simplebanking.screens.createTransaction.TransactionCreationStatus.SUCCESS
import com.example.simplebanking.screens.createTransaction.TransactionCreationStatus.TRANSACTION_INITIATED
import com.example.simplebanking.domain.Currency
import com.example.simplebanking.screens.createTransaction.TransactionCreationStatus.IDLE
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class CreateTransactionViewModel(
    private val firestoreRepository: FirestoreRepository,
    private val paymentsApi: PaymentsApi,
) : ViewModel() {

    private val _uiState = MutableStateFlow(TransactionCreationUIState())
    val uiState: StateFlow<TransactionCreationUIState> = _uiState.asStateFlow()

    fun onMessageShown() {
        _uiState.update {
            it.copy(
                status = if (it.status == ERROR) IDLE else it.status,
                message = null
            )
        }
    }

    fun submitTransaction(
        email: String,
        amount: Double,
        currency: Currency
    ) {
        if (validateTransactionDetails(email, amount, currency.name)) {
            _uiState.update {
                it.copy(
                    status = TRANSACTION_INITIATED,
                    message = "Transaction initiated. Please wait..."
                )
            }
            viewModelScope.launch {
                firestoreRepository.createTransaction(
                    FirestoreTransactionObject(
                        email = email,
                        amount = amount,
                        currency = currency.name
                    )
                )

                val apiResult = paymentsApi.createTransaction(
                    email = email,
                    amount = amount,
                    currency = currency
                )
                _uiState.update {
                    val message = if (apiResult.isFailure) {
                        apiResult.exceptionOrNull()?.message
                    } else {
                        apiResult.getOrNull()
                    }
                    it.copy(
                        status = if (apiResult.isFailure) ERROR else SUCCESS,
                        message = message
                    )
                }
            }
        } else {
            _uiState.update {
                it.copy(
                    status = ERROR,
                    message = "Invalid parameters entered. Please fix and try again."
                )
            }
        }
    }

    private fun validateTransactionDetails(email: String, amount: Double, currency: String) =
        validateEmail(email) && amount > 0.0 && Currency.isSupported(currency)

    private fun validateEmail(email: String) =
        email.isNotEmpty() && email.isNotBlank() && email.matches(EMAIL_REGEXP)

    private companion object {
        val EMAIL_REGEXP = Regex(
            "[a-zA-Z0-9+._%\\-]{1,256}" +
                    "@" +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,64}" +
                    "(" +
                    "\\." +
                    "[a-zA-Z0-9][a-zA-Z0-9\\-]{0,25}" +
                    ")+"
        )
    }
}
