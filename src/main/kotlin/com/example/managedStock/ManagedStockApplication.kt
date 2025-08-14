package com.example.managedStock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
@EnableScheduling
class ManagedStockApplication

fun main(args: Array<String>) {
	runApplication<ManagedStockApplication>(*args)
}
