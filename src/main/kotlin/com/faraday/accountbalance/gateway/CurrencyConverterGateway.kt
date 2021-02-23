package com.faraday.accountbalance.gateway

import org.joda.money.CurrencyUnit
import org.springframework.stereotype.Component
import java.math.BigDecimal

@Component
class CurrencyConverterGateway(val api: CurrencyGatewayApi) {

    fun getCurrentCurrencyRate(currency: CurrencyUnit): BigDecimal {
        val dto = api.getCurrencyRate(currency.code.toLowerCase())
        val midRate = dto?.rates?.first()?.mid
        return BigDecimal(midRate)
    }
}

data class ExchangeRateDto(val rates: List<Rate>)

data class Rate(val mid: String)
