package com.example.simplebanking.di

import com.example.simplebanking.api.FirestoreRepository
import com.example.simplebanking.api.FirestoreRepositoryImpl
import com.example.simplebanking.api.KtorPaymentsApi
import com.example.simplebanking.api.PaymentsApi
import com.example.simplebanking.screens.createTransaction.CreateTransactionViewModel
import com.example.simplebanking.screens.transactionList.TransactionListViewModel
import io.ktor.client.HttpClient
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.http.ContentType
import io.ktor.serialization.kotlinx.json.json
import kotlinx.serialization.json.Json
import org.koin.core.context.startKoin
import org.koin.core.module.dsl.factoryOf
import org.koin.dsl.module

val dataModule = module {
    single {
        val json = Json { ignoreUnknownKeys = true }
        HttpClient {
            install(ContentNegotiation) {
                json(json, contentType = ContentType.Any)
            }
        }
    }

    single<FirestoreRepository> { FirestoreRepositoryImpl()  }
    single<PaymentsApi> { KtorPaymentsApi(get()) }
}

val viewModelModule = module {
    factoryOf(::CreateTransactionViewModel)
    factoryOf(::TransactionListViewModel)
}

fun initKoin() {
    startKoin {
        modules(
            dataModule,
            viewModelModule,
        )
    }
}
