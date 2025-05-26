package com.example.simplebanking.domain

enum class Currency {
    USD, EUR;

    companion object {
        fun isSupported(currency: String): Boolean {
            return currency.isNotBlank()
                    && currency.isNotEmpty()
                    && entries.map { it.name }.contains(currency)
        }
    }
}