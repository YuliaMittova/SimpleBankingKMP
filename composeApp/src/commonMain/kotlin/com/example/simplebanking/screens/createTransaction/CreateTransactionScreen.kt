package com.example.simplebanking.screens.createTransaction

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.simplebanking.domain.Currency
import org.koin.compose.viewmodel.koinViewModel

@Composable
fun CreateTransactionScreen(
    navigateBack: () -> Unit,
) {
    val viewModel = koinViewModel<CreateTransactionViewModel>()
    val state by viewModel.uiState.collectAsStateWithLifecycle(
        initialValue = TransactionCreationUIState(TransactionCreationStatus.IDLE)
    )

    CreateTransactionComponent(
        state = state,
        onBackClick = navigateBack,
        onMessageShown = { viewModel.onMessageShown() },
        submitTransaction = { email, amount, currency ->
            viewModel.submitTransaction(
                email,
                amount,
                Currency.valueOf(currency)
            )
        },
    )
}
