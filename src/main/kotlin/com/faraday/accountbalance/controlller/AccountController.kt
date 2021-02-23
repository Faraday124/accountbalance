package com.faraday.accountbalance.controlller

import com.faraday.accountbalance.service.AccountBalanceService
import mu.KotlinLogging
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import javax.validation.constraints.Size

private val log = KotlinLogging.logger {}

@Validated
@RestController
class AccountController(val accountBalanceService: AccountBalanceService) {

    @GetMapping("/accounts/balances")
    fun getAccountBalance(@RequestParam @Size(min = 3, max = 3) currency: String = "usd",
                          @RequestBody accountId: String): String {
        log.info("[UserLog] Getting account balance for account number: $accountId in $currency")
        return accountBalanceService.getAccountBalance(accountId, currency)
    }
}

