package com.example.managedStock

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class ManagedStockApplication

fun main(args: Array<String>) {
	runApplication<ManagedStockApplication>(*args)
}
