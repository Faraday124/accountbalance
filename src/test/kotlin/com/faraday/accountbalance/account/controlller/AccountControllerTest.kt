package com.faraday.accountbalance.account.controlller

import com.faraday.accountbalance.account.service.AccountBalanceService
import com.faraday.accountbalance.config.CurrencyApiConfiguration
import org.joda.money.CurrencyUnit.GBP
import org.joda.money.CurrencyUnit.USD
import org.joda.money.Money
import org.junit.jupiter.api.Test
import org.junit.jupiter.api.extension.ExtendWith
import org.mockito.Mockito
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.boot.autoconfigure.EnableAutoConfiguration
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean
import org.springframework.http.MediaType
import org.springframework.test.context.junit.jupiter.SpringExtension
import org.springframework.test.web.servlet.MockMvc
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders
import org.springframework.test.web.servlet.result.MockMvcResultMatchers
import org.springframework.test.web.servlet.result.MockMvcResultMatchers.content
import java.math.BigDecimal

@ExtendWith(SpringExtension::class)
@SpringBootTest(classes = [AccountController::class, CurrencyApiConfiguration::class, AccountBalanceServiceConfiguration::class])
@AutoConfigureMockMvc
class AccountControllerTest {

    @Autowired
    lateinit var accountBalanceService: AccountBalanceService

    @Autowired
    lateinit var mvc: MockMvc

    companion object {
        const val ACCOUNT_ID = "123456"
    }

    @Test
    fun `should return balance in USD`() {
        //given
        val currency = "usd"
        val expectedMoney = Money.of(USD, BigDecimal("45678.98"))
        Mockito.`when`(accountBalanceService.getAccountBalance(ACCOUNT_ID, currency)).thenReturn(expectedMoney)

        //when
        val call = mvc.perform(MockMvcRequestBuilders.get("/accounts/$ACCOUNT_ID/balance?currency=$currency")
                .accept(MediaType.APPLICATION_JSON))

        //then
        call.andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(content().json("""
                    {
                    "amount": 45678.98,
                    "currency": "USD"
                    }
                """.trimIndent()))
    }

    @Test
    fun `should return correct balance when currency code is capitalized`() {
        //given
        val currency = "Gbp"
        val expectedMoney = Money.of(GBP, BigDecimal("45678.98"))
        Mockito.`when`(accountBalanceService.getAccountBalance(ACCOUNT_ID, currency)).thenReturn(expectedMoney)

        //when
        val call = mvc.perform(MockMvcRequestBuilders.get("/accounts/$ACCOUNT_ID/balance?currency=$currency")
                .accept(MediaType.APPLICATION_JSON))

        //then
        call.andExpect(MockMvcResultMatchers.status().isOk)
                .andExpect(content().json("""
                    {
                    "amount": 45678.98,
                    "currency": "GBP"
                    }
                """.trimIndent()))
    }

    @Test
    fun `should return 400 when no currency specified`() {
        //when
        val call = mvc.perform(MockMvcRequestBuilders.get("/accounts/$ACCOUNT_ID/balance")
                .accept(MediaType.APPLICATION_JSON))

        //then
        call.andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `should return 400 when invalid currency code`() {
        //given
        val currency = "usd123"

        //when
        val call = mvc.perform(MockMvcRequestBuilders.get("/accounts/$ACCOUNT_ID/balance?currency=$currency")
                .accept(MediaType.APPLICATION_JSON))

        //then
        call.andExpect(MockMvcResultMatchers.status().isBadRequest)
    }

    @Test
    fun `should return 400 when invalid accountId`() {
        //given
        val accountId = "accountId"
        val currency = "usd"

        //when
        val call = mvc.perform(MockMvcRequestBuilders.get("/accounts/$accountId/balance?currency=$currency")
                .accept(MediaType.APPLICATION_JSON))

        //then
        call.andExpect(MockMvcResultMatchers.status().isBadRequest)
    }
}

@EnableAutoConfiguration
@TestConfiguration
class AccountBalanceServiceConfiguration {

    @Bean
    fun accountBalanceService(): AccountBalanceService = Mockito.mock(AccountBalanceService::class.java)
}
