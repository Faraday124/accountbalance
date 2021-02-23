package com.faraday.accountbalance.repository

import org.joda.money.CurrencyUnit
import org.joda.money.Money
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal

interface AccountBalanceRepository {
    fun get(accountNumber: String): Money
}

@Component
class InMemoryAccountBalancePLNRepository : AccountBalanceRepository {
    companion object {
        val db: Map<String, String> = hashMapOf("123" to "4230.30")
    }

    override fun get(accountNumber: String): Money {
        if(db[accountNumber].isNullOrBlank()) {
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid account number")
        }

        val balance = BigDecimal(db[accountNumber])
        return Money.of(CurrencyUnit.of("PLN"), balance)
    }
}
