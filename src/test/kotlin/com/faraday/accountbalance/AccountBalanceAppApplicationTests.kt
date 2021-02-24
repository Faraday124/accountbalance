package com.faraday.accountbalance

import okhttp3.OkHttpClient
import org.junit.jupiter.api.Test
import org.springframework.boot.test.context.SpringBootTest
import org.springframework.boot.test.context.TestConfiguration
import org.springframework.context.annotation.Bean

@SpringBootTest
class AccountBalanceAppApplicationTests {

	@Test
	fun contextLoads() {
	}

	@TestConfiguration
	class CurrencyGatewayConfiguration {
		@Bean
		fun getTestHttpClient(): OkHttpClient = OkHttpClient.Builder().build()
	}

}
