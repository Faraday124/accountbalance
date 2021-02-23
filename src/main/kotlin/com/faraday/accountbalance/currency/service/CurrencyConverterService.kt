package com.faraday.accountbalance.currency.service

import com.faraday.accountbalance.currency.gateway.CurrencyConverterGateway
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import org.springframework.stereotype.Service
import java.math.RoundingMode

@Service
class CurrencyConverterService(val gateway: CurrencyConverterGateway) {

    fun convertToCurrency(balance: Money, currency: CurrencyUnit): Money {
        val currentRate = gateway.getCurrencyRate(currency)
        return balance.convertedTo(currency, currentRate, RoundingMode.HALF_UP)
    }
}
