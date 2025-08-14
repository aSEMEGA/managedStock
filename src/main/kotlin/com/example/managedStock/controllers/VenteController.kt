package com.example.managedStock.controllers

import com.example.managedStock.dto.VenteDto
import com.example.managedStock.services.VenteService
import jakarta.validation.Valid
import org.springframework.format.annotation.DateTimeFormat
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*
import java.time.LocalDateTime

@RestController
@RequestMapping("/api/v1/ventes")
class VenteController(private val venteService: VenteService) {

    @GetMapping
    fun getAll(): ResponseEntity<List<VenteDto>> = 
        ResponseEntity.ok(venteService.getAll())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int): ResponseEntity<VenteDto> = 
        ResponseEntity.ok(venteService.getVenteById(id))

    @PostMapping
    fun create(@Valid @RequestBody venteDto: VenteDto): ResponseEntity<VenteDto> =
        ResponseEntity.status(201).body(venteService.createVente(venteDto))

    @GetMapping("/product/{productId}")
    fun getByProduct(@PathVariable productId: Int): ResponseEntity<List<VenteDto>> =
        ResponseEntity.ok(venteService.getVentesByProduct(productId))

    @GetMapping("/date-range")
    fun getByDateRange(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: LocalDateTime
    ): ResponseEntity<List<VenteDto>> =
        ResponseEntity.ok(venteService.getVentesByDateRange(startDate, endDate))

    @GetMapping("/revenue")
    fun getTotalRevenue(
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) startDate: LocalDateTime,
        @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) endDate: LocalDateTime
    ): ResponseEntity<Map<String, Int>> =
        ResponseEntity.ok(mapOf("totalRevenue" to venteService.getTotalRevenue(startDate, endDate)))

    @GetMapping("/most-sold")
    fun getMostSoldProducts(@RequestParam(defaultValue = "10") limit: Int): ResponseEntity<List<Map<String, Any>>> =
        ResponseEntity.ok(venteService.getMostSoldProducts(limit))

    @GetMapping("/low-stock")
    fun getLowStockProducts(): ResponseEntity<List<com.example.managedStock.dto.ProductDto>> =
        ResponseEntity.ok(venteService.getLowStockProducts())

    @GetMapping("/credit")
    fun getVentesEnCredit(): ResponseEntity<List<VenteDto>> =
        ResponseEntity.ok(venteService.getVentesEnCredit())

    @GetMapping("/credit/client/{clientId}")
    fun getVentesEnCreditParClient(@PathVariable clientId: Int): ResponseEntity<List<VenteDto>> =
        ResponseEntity.ok(venteService.getVentesEnCreditParClient(clientId))

    @PostMapping("/{venteId}/payer-credit")
    fun payerCredit(@PathVariable venteId: Int, @RequestBody request: Map<String, Int>): ResponseEntity<VenteDto> {
        val montantPaye = request["montantPaye"] ?: throw IllegalArgumentException("Montant payé requis")
        return ResponseEntity.ok(venteService.payerCredit(venteId, montantPaye))
    }

    @PostMapping("/credit/rappels")
    fun envoyerRappelsCredit(): ResponseEntity<Map<String, String>> {
        venteService.envoyerRappelsCredit()
        return ResponseEntity.ok(mapOf("message" to "Rappels de crédit envoyés"))
    }

    @GetMapping("/credit/total")
    fun getTotalCreditEnAttente(): ResponseEntity<Map<String, Int>> =
        ResponseEntity.ok(mapOf("totalCredit" to venteService.getTotalCreditEnAttente()))
}