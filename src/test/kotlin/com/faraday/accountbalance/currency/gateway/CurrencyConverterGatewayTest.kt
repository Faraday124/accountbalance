package com.faraday.accountbalance.currency.gateway

import com.faraday.accountbalance.currency.exception.InvalidCurrencyRateException
import org.assertj.core.api.Assertions.assertThat
import org.joda.money.CurrencyUnit.USD
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.mockito.Mockito

class CurrencyConverterGatewayTest {

    private val currencyGatewayApi = Mockito.mock(CurrencyGatewayApi::class.java)


    @Test
    fun `should return correct currency rate`() {

        //given
        val expectedCurrencyRate = "3.71"
        val exchangeRate = ExchangeRateDto(listOf(Rate(expectedCurrencyRate)))
        val currencyConverterGateway = CurrencyConverterGateway(currencyGatewayApi)
        Mockito.`when`(currencyGatewayApi.getCurrencyRate("usd")).thenReturn(exchangeRate)

        //when
        val result = currencyConverterGateway.getCurrencyRate(USD)

        //then
        assertThat(result).isEqualTo(expectedCurrencyRate)
    }

    @Test
    fun `should throw error when incorrect currency rate`() {

        //given
        val exchangeRate = ExchangeRateDto(listOf())
        val currencyConverterGateway = CurrencyConverterGateway(currencyGatewayApi)
        Mockito.`when`(currencyGatewayApi.getCurrencyRate("usd")).thenReturn(exchangeRate)

        //when
        //then
        assertThrows<InvalidCurrencyRateException> {
            currencyConverterGateway.getCurrencyRate(USD)
        }
    }
}
