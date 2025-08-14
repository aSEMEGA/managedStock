package com.example.managedStock.controllers

import com.example.managedStock.dto.MouvStockDto
import com.example.managedStock.services.MouvStockService
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/mouvements")
class MouvStockController(private val mouvStockService: MouvStockService) {

    @GetMapping
    fun getAll(): ResponseEntity<List<MouvStockDto>> = ResponseEntity.ok(mouvStockService.getAll())

    @GetMapping("/product/{productId}")
    fun getByProduct(@PathVariable productId: Int): ResponseEntity<List<MouvStockDto>> =
        ResponseEntity.ok(mouvStockService.getByProduct(productId))
}
