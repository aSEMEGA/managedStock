package com.example.managedStock.repository

import com.example.managedStock.entities.Vente
import org.springframework.data.jpa.repository.JpaRepository

interface VenteRepository : JpaRepository<Vente, Int>{
}