package com.example.simplebanking.screens.transactionList

import app.cash.turbine.test
import com.example.simplebanking.data.FirestoreTransactionObject
import com.example.simplebanking.screens.faker.FakeFirestoreRepository
import dev.gitlive.firebase.firestore.Timestamp
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals
import kotlin.test.assertNull

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionListViewModelTest {

    @BeforeTest
    fun setUp() {
        // Set the main dispatcher for testing
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        Dispatchers.resetMain()
    }

    @Test
    fun testTransactionLoading() = runTest {
        val firestoreRepository = FakeFirestoreRepository()
        firestoreRepository.createTransaction(FirestoreTransactionObject(
            email = TEST_EMAIL,
            amount = TEST_AMOUNT,
            currency = TEST_CURRENCY,
            timestamp = Timestamp.now(),
        ))
        val testViewModel = TransactionListViewModel(
            firestoreRepository = firestoreRepository
        )

        testViewModel.uiState.test {
            // Create a test transaction
            testViewModel.loadTransactions()

            val result = expectMostRecentItem()
            assertEquals(TransactionListStatus.SUCCESS, result.status)
            assertNull(result.errorMessage)
            assertEquals(1, result.transactionData.size)
            val resultTransaction = result.transactionData.first()
            assertEquals(TEST_EMAIL, resultTransaction.email)
            assertEquals(TEST_AMOUNT, resultTransaction.amount)
            assertEquals(TEST_CURRENCY, resultTransaction.currency.name)
        }
    }

    private companion object {
        const val TEST_EMAIL = "test@gmail.com"
        const val TEST_AMOUNT = 100.04
        const val TEST_CURRENCY = "EUR"
    }
}