package com.faraday.accountbalance.account.controlller

import com.faraday.accountbalance.account.service.AccountBalanceService
import mu.KotlinLogging
import org.joda.money.Money
import org.springframework.http.HttpStatus
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal

private val log = KotlinLogging.logger {}

@RestController
class AccountController(val accountBalanceService: AccountBalanceService) {

    @GetMapping("/accounts/{accountId}/balance")
    fun getAccountBalance(@PathVariable accountId: String, @RequestParam currency: String): AccountBalanceDto {
        validateParams(accountId, currency)

        log.info("[UserLog] Getting account balance for account number: $accountId in $currency")
        val balance = accountBalanceService.getAccountBalance(accountId, currency)
        return balance.toDTO()
    }

    private fun validateParams(accountId: String, currency: String) {
        validateParam(accountId, Regex("^\\d{6}$"), "Invalid account number")
        validateParam(currency, Regex("^[A-Za-z]{3}$"), "Invalid currency code")
    }

    private fun validateParam(param: String, regex: Regex, errMessage: String) {
        if (!param.matches(regex)) {
            log.error("$errMessage: $param")
            throw ResponseStatusException(HttpStatus.BAD_REQUEST, errMessage)
        }
    }
}

fun Money.toDTO(): AccountBalanceDto = AccountBalanceDto(this.amount, this.currencyUnit.toString())

data class AccountBalanceDto(val amount: BigDecimal, val currency: String)

