package com.example.simplebanking.api

import com.example.simplebanking.data.CreateTransactionRequest
import com.example.simplebanking.domain.Currency
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.request.post
import io.ktor.client.request.setBody
import io.ktor.client.statement.HttpResponse
import io.ktor.http.ContentType
import io.ktor.http.HttpStatusCode
import io.ktor.http.contentType

interface PaymentsApi {
    suspend fun createTransaction(
        email: String, amount: Double, currency: Currency
    ): Result<String>
}

class KtorPaymentsApi(private val client: HttpClient) : PaymentsApi {

    override suspend fun createTransaction(
        email: String, amount: Double, currency: Currency
    ): Result<String> {
        return try {
            val response: HttpResponse = client.post(API_URL) {
                contentType(ContentType.Application.Json)
                setBody(
                    CreateTransactionRequest(
                        recipientEmail = email,
                        amount = amount,
                        currency = currency.name
                    )
                )
            }
            if (response.status == HttpStatusCode.OK) {
                Result.success(response.body())
            } else {
                // in case of bad request
                Result.failure(Exception(response.body<String>()))
            }
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    private companion object {
        const val API_URL = "https://my-payments-server.onrender.com/payments"
    }
}
