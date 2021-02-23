package com.faraday.accountbalance.service

import com.faraday.accountbalance.gateway.CurrencyConverterGateway
import org.joda.money.CurrencyUnit
import org.joda.money.Money
import org.springframework.stereotype.Service
import java.math.RoundingMode

interface CurrencyConverterService {
    fun convertToCurrency(balanceInPLN: Money, currency: CurrencyUnit): Money
}

@Service
class CurrencyConverterServiceImpl(val gateway: CurrencyConverterGateway) : CurrencyConverterService {

    override fun convertToCurrency(balanceInPLN: Money, currency: CurrencyUnit): Money {
        val currentRate = gateway.getCurrentCurrencyRate(currency)
        return balanceInPLN.convertedTo(currency, currentRate, RoundingMode.HALF_UP)
    }
}
