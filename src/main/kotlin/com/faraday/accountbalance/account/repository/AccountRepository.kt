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
        val DB: Map<String, Account> = hashMapOf(
                "12345" to Account("54321", "0000 0000 0000 0000 0001",
                        Money.of(CurrencyUnit.of("PLN"), BigDecimal("4000.32"))
                ),
                "54321" to Account("12345", "0000 0000 0000 0000 0009",
                        Money.of(CurrencyUnit.of("PLN"), BigDecimal("10.02"))
                ),
                "11111" to Account("11112", "0000 0000 0000 0000 0000",
                        Money.of(CurrencyUnit.of("PLN"), BigDecimal.ZERO)
                ),
        )
    }

    override fun get(accountNumber: String): Account = DB.getOrElse(accountNumber) {
        throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid account number")
    }
}
