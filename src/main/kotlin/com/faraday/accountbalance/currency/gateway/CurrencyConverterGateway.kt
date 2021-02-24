package com.faraday.accountbalance.currency.gateway

import com.faraday.accountbalance.currency.exception.InvalidCurrencyRateException
import org.joda.money.CurrencyUnit
import org.springframework.stereotype.Component
import java.lang.RuntimeException
import java.math.BigDecimal

@Component
class CurrencyConverterGateway(val api: CurrencyGatewayApi) {

    fun getCurrencyRate(currency: CurrencyUnit): BigDecimal {
        val dto = api.getCurrencyRate(currency.code.toLowerCase())
        if(dto?.rates == null || dto.rates.isEmpty()) {
            throw InvalidCurrencyRateException("Invalid result currency rate for currency: $currency")
        }
        val midRate = dto.rates.first().mid
        return BigDecimal(midRate)
    }
}

data class ExchangeRateDto(val rates: List<Rate>)

data class Rate(val mid: String)
