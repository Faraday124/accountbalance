package com.faraday.accountbalance

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class AccountBalanceAppApplication

fun main(args: Array<String>) {
	runApplication<AccountBalanceAppApplication>(*args)
}
