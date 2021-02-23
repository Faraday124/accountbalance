package com.faraday.accountbalance.account.model

import org.joda.money.Money

data class Account(
        val accountId: String,
        val accountNumber: String,
        val balance: Money
)
