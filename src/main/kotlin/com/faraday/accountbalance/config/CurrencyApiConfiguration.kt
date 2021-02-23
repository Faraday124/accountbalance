package com.faraday.accountbalance.config

import okhttp3.OkHttpClient
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
@EnableConfigurationProperties(CurrencyApiProperties::class)
class CurrencyApiConfiguration {
    @Bean
    fun getHttpClient() : OkHttpClient = OkHttpClient()
}

@ConfigurationProperties("currency.api")
class CurrencyApiProperties {
    lateinit var url: String
}


