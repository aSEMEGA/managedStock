package com.example.managedStock.controllers

import com.example.managedStock.dto.ClientDto
import com.example.managedStock.services.ClientService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/clients")
class ClientController(private val clientService: ClientService) {

    @PostMapping
    fun createClient(@Valid @RequestBody clientDto: ClientDto): ResponseEntity<ClientDto> =
        ResponseEntity.status(201).body(clientService.createClient(clientDto))

    @GetMapping
    fun getAllClients(): ResponseEntity<List<ClientDto>> =
        ResponseEntity.ok(clientService.getAllClients())

    @GetMapping("/active")
    fun getActiveClients(): ResponseEntity<List<ClientDto>> =
        ResponseEntity.ok(clientService.getActiveClients())

    @GetMapping("/{id}")
    fun getClientById(@PathVariable id: Int): ResponseEntity<ClientDto> =
        ResponseEntity.ok(clientService.getClientById(id))

    @PutMapping("/{id}")
    fun updateClient(@PathVariable id: Int, @Valid @RequestBody clientDto: ClientDto): ResponseEntity<ClientDto> =
        ResponseEntity.ok(clientService.updateClient(id, clientDto))

    @DeleteMapping("/{id}")
    fun deleteClient(@PathVariable id: Int): ResponseEntity<Void> {
        clientService.deleteClient(id)
        return ResponseEntity.noContent().build()
    }

    @GetMapping("/search")
    fun searchClients(@RequestParam q: String): ResponseEntity<List<ClientDto>> =
        ResponseEntity.ok(clientService.searchClients(q))
}
