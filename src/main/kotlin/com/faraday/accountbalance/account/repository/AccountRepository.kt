package com.faraday.accountbalance.account.repository

import com.faraday.accountbalance.account.model.Account
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal

interface AccountRepository {
    fun get(accountNumber: String): Account
}

@Component
class InMemoryAccountRepository : AccountRepository {
    companion object {
        private val CURRENCY_UNIT_PLN = CurrencyUnit.of("PLN")
        val DB: Map<String, Account> = hashMapOf(
                "123456" to Account("654321", "0000 0000 0000 0000 0001",
                        Money.of(CURRENCY_UNIT_PLN, BigDecimal("4000.32"))
                ),
                "654321" to Account("123456", "0000 0000 0000 0000 0009",
                        Money.of(CURRENCY_UNIT_PLN, BigDecimal("10.02"))
                ),
                "111111" to Account("111112", "0000 0000 0000 0000 0000",
                        Money.of(CURRENCY_UNIT_PLN, BigDecimal.ZERO)
                ),
        )
    }

    override fun get(accountNumber: String): Account = DB.getOrElse(accountNumber) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid account number")
    }
}
