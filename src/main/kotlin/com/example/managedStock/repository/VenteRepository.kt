package com.example.managedStock.repository

import com.example.managedStock.entities.Vente
import com.example.managedStock.entities.Product
import com.example.managedStock.entities.Client
import com.example.managedStock.entities.Users
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query
import org.springframework.data.repository.query.Param
import java.time.LocalDateTime

interface VenteRepository : JpaRepository<Vente, Int>{
    
    fun findByProduct(product: Product): List<Vente>
    
    fun findByDatecreationVenteBetween(startDate: LocalDateTime, endDate: LocalDateTime): List<Vente>
    
    fun findByIsCreditTrueAndIsPayeFalse(): List<Vente>
    
    fun findByClientAndIsCreditTrueAndIsPayeFalse(client: Client): List<Vente>
    
    fun findByIsCreditTrueAndIsPayeFalseAndDateEcheanceBefore(date: LocalDateTime): List<Vente>
    
    fun findByClient(client: Client): List<Vente>
    
    fun findByVendeur(vendeur: Users): List<Vente>
    
    @Query("""
        SELECT p.nom as productName, SUM(v.quantite) as totalQuantity, SUM(v.total) as totalRevenue
        FROM Vente v 
        JOIN v.product p 
        GROUP BY p.id, p.nom 
        ORDER BY totalQuantity DESC 
        LIMIT :limit
    """)
    fun findMostSoldProducts(@Param("limit") limit: Int): List<Map<String, Any>>
}