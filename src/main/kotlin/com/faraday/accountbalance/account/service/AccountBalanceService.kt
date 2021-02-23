package com.faraday.accountbalance.account.service

import com.faraday.accountbalance.currency.service.CurrencyConverterService
import mu.KotlinLogging
import org.joda.money.CurrencyUnit
import org.joda.money.IllegalCurrencyException
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Service
import org.springframework.web.server.ResponseStatusException

private val log = KotlinLogging.logger {}

@Service
class AccountBalanceService(val accountBalanceProvider: AccountBalanceProvider, val converterService: CurrencyConverterService) {
    fun getAccountBalance(accountId: String, currency: String): String {
        val currencyUnit = mapToCurrencyUnit(currency)
        val balance = accountBalanceProvider.getAccountBalance(accountId)
        val convertedBalance = converterService.convertToCurrency(balance, currencyUnit)
        return convertedBalance.toString()
    }

    private fun mapToCurrencyUnit(currency: String): CurrencyUnit {
        try {
            return CurrencyUnit.of(currency.toUpperCase())
        } catch (e: IllegalCurrencyException) {
            log.error { "Currency $currency is not supported" }
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, "Currency $currency is not supported", e)
        }
    }
}
