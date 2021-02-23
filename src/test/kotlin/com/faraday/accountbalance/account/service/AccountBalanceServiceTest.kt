package com.faraday.accountbalance.account.service

import com.faraday.accountbalance.currency.service.CurrencyConverterService
import org.assertj.core.api.Assertions.assertThat
import org.joda.money.CurrencyUnit
import org.joda.money.CurrencyUnit.GBP
import org.joda.money.CurrencyUnit.USD
import org.joda.money.Money
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito
import org.springframework.web.server.ResponseStatusException
import java.math.BigDecimal
import java.math.MathContext

class AccountBalanceServiceTest {
    private val accountBalanceProvider: AccountBalanceProvider = Mockito.mock(AccountBalanceProvider::class.java)
    private val converterService: CurrencyConverterService = Mockito.mock(CurrencyConverterService::class.java)

    companion object {
        const val ACCOUNT_ID = "12345"
    }

    @Test
    fun `should return account balance in USD`() {
        //given
        val balanceInPLN = Money.of(CurrencyUnit.of("PLN"), BigDecimal("1234"))
        val expectedResult = Money.of(USD, BigDecimal(13697))

        Mockito.`when`(accountBalanceProvider.getAccountBalance(ACCOUNT_ID)).thenReturn(balanceInPLN)
        Mockito.`when`(converterService.convertToCurrency(balanceInPLN, USD)).thenReturn(expectedResult)
        val accountBalanceService = AccountBalanceService(accountBalanceProvider, converterService)

        //when
        val result = accountBalanceService.getAccountBalance(ACCOUNT_ID, "usd")

        //then
        assertThat(result).isEqualTo(expectedResult)

    }

    @Test
    fun `should return account balance in USD with precision`() {
        //given
        val balanceInPLN = Money.of(CurrencyUnit.of("PLN"), BigDecimal("1234.76"))
        val expectedResult = Money.of(USD, BigDecimal("13697.99"))

        Mockito.`when`(accountBalanceProvider.getAccountBalance(ACCOUNT_ID)).thenReturn(balanceInPLN)
        Mockito.`when`(converterService.convertToCurrency(balanceInPLN, USD)).thenReturn(expectedResult)
        val accountBalanceService = AccountBalanceService(accountBalanceProvider, converterService)

        //when
        val result = accountBalanceService.getAccountBalance(ACCOUNT_ID, "usd")

        //then
        assertThat(result).isEqualTo(expectedResult)

    }

    @Test
    fun `should return negative balance when customer has debt`() {
        //given
        val balanceInPLN = Money.of(CurrencyUnit.of("PLN"), BigDecimal("-1234.76"))
        val expectedResult = Money.of(USD, BigDecimal("-13697.99"))

        Mockito.`when`(accountBalanceProvider.getAccountBalance(ACCOUNT_ID)).thenReturn(balanceInPLN)
        Mockito.`when`(converterService.convertToCurrency(balanceInPLN, USD)).thenReturn(expectedResult)
        val accountBalanceService = AccountBalanceService(accountBalanceProvider, converterService)

        //when
        val result = accountBalanceService.getAccountBalance(ACCOUNT_ID, "usd")

        //then
        assertThat(result).isEqualTo(expectedResult)

    }

    @Test
    fun `should return account balance in GBP converted from USD`() {
        //given
        val balanceInUSD = Money.of(USD, BigDecimal("1234.76"))
        val expectedResult = Money.of(GBP, BigDecimal("13697.99"))

        Mockito.`when`(accountBalanceProvider.getAccountBalance(ACCOUNT_ID)).thenReturn(balanceInUSD)
        Mockito.`when`(converterService.convertToCurrency(balanceInUSD, GBP)).thenReturn(expectedResult)
        val accountBalanceService = AccountBalanceService(accountBalanceProvider, converterService)

        //when
        val result = accountBalanceService.getAccountBalance(ACCOUNT_ID, "gbp")

        //then
        assertThat(result).isEqualTo(expectedResult)

    }

    @Test
    fun `should return zero when balance is zero`() {
        //given
        val balanceInUSD = Money.of(USD, BigDecimal.ZERO)
        val expectedResult = Money.of(GBP, BigDecimal.ZERO)

        Mockito.`when`(accountBalanceProvider.getAccountBalance(ACCOUNT_ID)).thenReturn(balanceInUSD)
        Mockito.`when`(converterService.convertToCurrency(balanceInUSD, GBP)).thenReturn(expectedResult)
        val accountBalanceService = AccountBalanceService(accountBalanceProvider, converterService)

        //when
        val result = accountBalanceService.getAccountBalance(ACCOUNT_ID, "gbp")

        //then
        assertThat(result).isEqualTo(expectedResult)

    }

    @Test
    fun `should throw exception when incorrect currency`() {
        //given
        val accountId = "12345"
        val accountBalanceService = AccountBalanceService(accountBalanceProvider, converterService)

        //when
        //then
        assertThrows<ResponseStatusException> {
            accountBalanceService.getAccountBalance(accountId, "usd123")
        }
    }
}
