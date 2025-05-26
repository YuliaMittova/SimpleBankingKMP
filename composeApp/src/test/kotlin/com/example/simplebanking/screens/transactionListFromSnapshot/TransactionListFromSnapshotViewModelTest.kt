package com.example.simplebanking.screens.transactionListFromSnapshot

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
import kotlin.test.assertNotNull

@OptIn(ExperimentalCoroutinesApi::class)
class TransactionListFromSnapshotViewModelTest {

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
        firestoreRepository.createTransaction(
            FirestoreTransactionObject(
                email = TEST_EMAIL,
                amount = TEST_AMOUNT,
                currency = TEST_CURRENCY,
                timestamp = Timestamp.now(),
            )
        )
        val testViewModel = TransactionListFromSnapshotViewModel(
            firestoreRepository = firestoreRepository
        )

        testViewModel.fetchTransactionsFromSnapshot().test {
            // Create a test transaction
            val result = expectMostRecentItem()
            assertEquals(TEST_EMAIL, result.email)
            assertEquals(TEST_AMOUNT, result.amount)
            assertEquals(TEST_CURRENCY, result.currency.name)
            assertNotNull(result.timestamp)
        }
    }

    private companion object {
        const val TEST_EMAIL = "test@gmail.com"
        const val TEST_AMOUNT = 100.04
        const val TEST_CURRENCY = "EUR"
    }
}