package com.example.simplebanking.screens.createTransaction

import app.cash.turbine.test
import com.example.simplebanking.api.FirestoreRepository
import com.example.simplebanking.api.PaymentsApi
import com.example.simplebanking.domain.Currency
import com.example.simplebanking.screens.faker.FakeFirestoreRepository
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.UnconfinedTestDispatcher
import kotlinx.coroutines.test.resetMain
import kotlinx.coroutines.test.runTest
import kotlinx.coroutines.test.setMain
import org.koin.core.context.startKoin
import org.koin.core.context.stopKoin
import org.koin.dsl.module
import org.koin.test.KoinTest
import org.koin.test.get
import kotlin.test.AfterTest
import kotlin.test.BeforeTest
import kotlin.test.Test
import kotlin.test.assertEquals

@OptIn(ExperimentalCoroutinesApi::class)
class CreateTransactionViewModelTest : KoinTest {

    private val testModule = module {
        single<FirestoreRepository> { FakeFirestoreRepository() }
    }

    @BeforeTest
    fun setUp() {
        startKoin {
            modules(testModule)
        }
        // Set the main dispatcher for testing
        Dispatchers.setMain(UnconfinedTestDispatcher())
    }

    @AfterTest
    fun tearDown() {
        stopKoin()
        Dispatchers.resetMain()
    }

    @Test
    fun testCreateTransactionWhenEnteredEmailIsInvalid() = runTest {
        val paymentsApi = FakePaymentsApi(expectedResult = Result.success("SUCCESS"))
        val testViewModel = CreateTransactionViewModel(
            firestoreRepository = get(),
            paymentsApi = paymentsApi
        )

        testViewModel.uiState.test {
            // Create a test transaction
            testViewModel.submitTransaction(
                email = "aaaaa.aaaaa",
                amount = TEST_AMOUNT,
                currency = Currency.EUR
            )

            val result = expectMostRecentItem()
            assertEquals(TransactionCreationStatus.ERROR, result.status)
            assertEquals("Invalid parameters entered. Please fix and try again.", result.message)
        }
    }

    @Test
    fun testCreateTransactionWhenEnteredAmountIsInvalid() = runTest {
        val paymentsApi = FakePaymentsApi(expectedResult = Result.success("SUCCESS"))
        val testViewModel = CreateTransactionViewModel(
            firestoreRepository = get(),
            paymentsApi = paymentsApi
        )

        testViewModel.uiState.test {
            // Create a test transaction
            testViewModel.submitTransaction(
                email = TEST_EMAIL,
                amount = 0.0,
                currency = Currency.EUR
            )

            val result = expectMostRecentItem()
            assertEquals(TransactionCreationStatus.ERROR, result.status)
            assertEquals("Invalid parameters entered. Please fix and try again.", result.message)
        }
    }

    @Test
    fun testCreateTransactionWhenPaymentApiFailed() = runTest {
        val paymentsApi = FakePaymentsApi(expectedResult = Result.failure(Exception("FAIL")))
        val testViewModel = CreateTransactionViewModel(
            firestoreRepository = get(),
            paymentsApi = paymentsApi
        )

        testViewModel.uiState.test {
            // Create a test transaction
            testViewModel.submitTransaction(
                email = TEST_EMAIL,
                amount = TEST_AMOUNT,
                currency = Currency.EUR
            )

            val result = expectMostRecentItem()
            assertEquals(TransactionCreationStatus.ERROR, result.status)
            assertEquals("FAIL", result.message)
        }
    }

    @Test
    fun testCreateTransactionWhenPaymentApiSuccess() = runTest {
        val paymentsApi = FakePaymentsApi(expectedResult = Result.success("Success"))
        val firestoreRepository = FakeFirestoreRepository()
        val testViewModel = CreateTransactionViewModel(
            firestoreRepository = firestoreRepository,
            paymentsApi = paymentsApi
        )

        testViewModel.uiState.test {
            // Create a test transaction
            testViewModel.submitTransaction(
                email = TEST_EMAIL,
                amount = TEST_AMOUNT,
                currency = Currency.EUR
            )

            val result = expectMostRecentItem()
            assertEquals(TransactionCreationStatus.SUCCESS, result.status)
            assertEquals("Success", result.message)
            // transaction was successfully added
            assertEquals(1, firestoreRepository.fetchTransactions().transactionList.size)
        }
    }

    class FakePaymentsApi(private val expectedResult: Result<String>) : PaymentsApi {
        override suspend fun createTransaction(
            email: String,
            amount: Double,
            currency: Currency
        ) = expectedResult
    }

    private companion object {
        const val TEST_EMAIL = "test@gmail.com"
        const val TEST_AMOUNT = 100.04
    }
}