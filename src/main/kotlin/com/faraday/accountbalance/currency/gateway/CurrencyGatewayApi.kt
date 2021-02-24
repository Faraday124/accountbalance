package com.faraday.accountbalance.currency.gateway

import com.faraday.accountbalance.config.CurrencyApiProperties
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.Request
import org.springframework.cache.annotation.CacheConfig
import org.springframework.cache.annotation.Cacheable
import org.springframework.http.HttpStatus
import org.springframework.stereotype.Component
import org.springframework.web.server.ResponseStatusException

interface CurrencyGatewayApi {
    fun getCurrencyRate(currency: String): ExchangeRateDto?
}

@CacheConfig(cacheNames = ["exchangeRates"])
@Component
class CurrencyGatewayApiImpl(val config: CurrencyApiProperties, val client: OkHttpClient) : CurrencyGatewayApi {

    companion object {
        const val TABLE_PREFIX = "a"
    }

    @Cacheable
    override fun getCurrencyRate(currency: String): ExchangeRateDto? {
        val request = Request.Builder().url("${config.url}/$TABLE_PREFIX/$currency").build()

        client.newCall(request).execute().use {
            if (!it.isSuccessful) throw ResponseStatusException(HttpStatus.NOT_FOUND, "Failed to fetch current rate for currency: $currency Error response: $it.response")

            val gson = GsonBuilder().create()
            return gson.fromJson(it.body?.string(), ExchangeRateDto::class.java)
        }
    }
}
