package com.example.managedStock.controllers

import com.example.managedStock.dto.VenteDto
import com.example.managedStock.services.VenteService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/ventes")
class VenteController(private val venteService: VenteService) {

    @GetMapping
    fun getAll() = ResponseEntity.ok(venteService.getAll())

    @PostMapping
    fun create(@Valid @RequestBody venteDto: VenteDto) =
        ResponseEntity.status(201).body(venteService.createVente(venteDto))
}