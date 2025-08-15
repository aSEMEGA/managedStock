package com.example.managedStock.repository

import com.example.managedStock.dto.VenteDto
import com.example.managedStock.entities.Client
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import java.time.LocalDateTime

interface ClientRepository : JpaRepository<Client, Int> {
    fun findByEmail(email: String): Client?
    fun findByNomContainingIgnoreCase(nom: String): List<Client>
    
    @Query("SELECT c FROM Client c WHERE c.isActive = true")
    fun findActiveClients(): List<Client>

    fun findByNomAndTelephone(nom: String, telephone: String): Client?
}
