package com.faraday.accountbalance.service

import com.faraday.accountbalance.repository.AccountBalanceRepository
import org.joda.money.Money
import org.springframework.stereotype.Service

@Service
class AccountBalanceProvider(val accountBalanceRepository: AccountBalanceRepository) {
    fun getAccountBalance(accountId: String): Money = accountBalanceRepository.get(accountId)
}
