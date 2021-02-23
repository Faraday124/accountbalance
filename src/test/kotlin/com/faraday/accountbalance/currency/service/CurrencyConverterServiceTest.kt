package com.faraday.accountbalance.currency.service

import com.faraday.accountbalance.currency.gateway.CurrencyConverterGateway
import org.assertj.core.api.Assertions.assertThat
import org.joda.money.CurrencyUnit
import org.joda.money.CurrencyUnit.USD
import org.joda.money.Money
import org.junit.jupiter.api.Test
import org.mockito.Mockito
import java.math.BigDecimal

class CurrencyConverterServiceTest {

    private val gateway: CurrencyConverterGateway = Mockito.mock(CurrencyConverterGateway::class.java)

    @Test
    fun `should convert currency with current rate`() {
        //given
        val balanceInPLN = Money.of(CurrencyUnit.of("PLN"), BigDecimal("1234.71"))
        val expectedResult = Money.of(USD, BigDecimal("4580.77"))
        Mockito.`when`(gateway.getCurrencyRate(USD)).thenReturn(BigDecimal("3.71"))

        val currencyConverterService = CurrencyConverterService(gateway)

        //when
        val result = currencyConverterService.convertToCurrency(balanceInPLN, USD)

        //then
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should convert currency when negative balance`() {
        //given
        val balanceInPLN = Money.of(CurrencyUnit.of("PLN"), BigDecimal("-1234.71"))
        val expectedResult = Money.of(USD, BigDecimal("-4580.77"))
        Mockito.`when`(gateway.getCurrencyRate(USD)).thenReturn(BigDecimal("3.71"))

        val currencyConverterService = CurrencyConverterService(gateway)

        //when
        val result = currencyConverterService.convertToCurrency(balanceInPLN, USD)

        //then
        assertThat(result).isEqualTo(expectedResult)
    }

    @Test
    fun `should convert currency when balance is equal zero`() {
        //given
        val balanceInPLN = Money.of(CurrencyUnit.of("PLN"), BigDecimal.ZERO)
        val expectedResult = Money.of(USD, BigDecimal.ZERO)
        Mockito.`when`(gateway.getCurrencyRate(USD)).thenReturn(BigDecimal("3.71"))

        val currencyConverterService = CurrencyConverterService(gateway)

        //when
        val result = currencyConverterService.convertToCurrency(balanceInPLN, USD)

        //then
        assertThat(result).isEqualTo(expectedResult)
    }
}
