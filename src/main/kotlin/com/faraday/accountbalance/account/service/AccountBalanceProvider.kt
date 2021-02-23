package com.faraday.accountbalance.account.service

import com.faraday.accountbalance.account.repository.AccountRepository
import org.joda.money.Money
import org.springframework.stereotype.Service

@Service
class AccountBalanceProvider(val accountRepository: AccountRepository) {
    fun getAccountBalance(accountId: String): Money {
        val bankAccount = accountRepository.get(accountId)
        return bankAccount.balance
    }
}
