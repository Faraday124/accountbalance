package com.faraday.accountbalance.account.service

import com.faraday.accountbalance.account.repository.AccountRepository
import org.springframework.stereotype.Service

@Service
class AccountBalanceProvider(val accountRepository: AccountRepository) {
    fun getAccountBalance(accountId: String) = accountRepository.get(accountId).balance
}
