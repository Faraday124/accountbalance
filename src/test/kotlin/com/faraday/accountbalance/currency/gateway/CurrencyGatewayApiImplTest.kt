package com.faraday.accountbalance.currency.gateway

import com.faraday.accountbalance.config.CurrencyApiProperties
import okhttp3.OkHttpClient
import okhttp3.mock.Behavior
import okhttp3.mock.MediaTypes.MEDIATYPE_JSON
import okhttp3.mock.MockInterceptor
import org.assertj.core.api.Assertions.assertThat
import org.junit.jupiter.api.BeforeEach
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.assertThrows
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.web.server.ResponseStatusException


@SpringBootTest
class CurrencyGatewayApiImplTest {

    @Autowired
    lateinit var properties: CurrencyApiProperties

    @Autowired
    lateinit var client: OkHttpClient

    val interceptor = MockInterceptor(Behavior.UNORDERED)

    @BeforeEach
    fun setup() {
        client = OkHttpClient.Builder()
                .addInterceptor(interceptor)
                .build()
    }

    @Test
    fun `should return correct exchange rate for USD`() {
        //given
        val rate = "5.26"
        val exchangeRateDto = ExchangeRateDto(listOf(Rate(rate)))

        val gateway: CurrencyGatewayApi = CurrencyGatewayApiImpl(properties, client)

        addInterceptorRule(rate)

        //when
        val result = gateway.getCurrencyRate("gbp")

        //then
        assertThat(result).isEqualTo(exchangeRateDto)
    }

    @Test
    fun `should return correct exchange rate for GBP`() {
        //given
        val rate = "3.7145"
        val exchangeRateDto = ExchangeRateDto(listOf(Rate(rate)))

        val gateway: CurrencyGatewayApi = CurrencyGatewayApiImpl(properties, client)

        addInterceptorRule(rate)

        //when
        val result = gateway.getCurrencyRate("usd")

        //then
        assertThat(result).isEqualTo(exchangeRateDto)
    }

    @Test
    fun `should return 404 when not found currency`() {
        //given
        val currency = "usd123"

        val gateway: CurrencyGatewayApi = CurrencyGatewayApiImpl(properties, client)

        interceptor.behavior(Behavior.SEQUENTIAL).addRule()
                .get().respond(404)

        //when
        //then
        assertThrows< ResponseStatusException> {
            gateway.getCurrencyRate(currency)
        }
    }

    private fun addInterceptorRule(rate: String) {
        interceptor.behavior(Behavior.SEQUENTIAL).addRule()
                .get().respond("""
                    {
                        "rates": [
                            {
                                "mid": $rate
                            }
                        ]
                    }
                    """.trimIndent(), MEDIATYPE_JSON)
    }


    @TestConfiguration
    class CurrencyGatewayConfiguration {
        @Bean
        fun getTestHttpClient(): OkHttpClient = OkHttpClient.Builder().build()
    }
}
