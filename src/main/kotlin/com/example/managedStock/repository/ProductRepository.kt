package com.example.managedStock.repository

import com.example.managedStock.dto.CategoryDto
import com.example.managedStock.dto.ProductDto
import com.example.managedStock.entities.Product
import com.example.managedStock.entities.Category
import org.springframework.data.jpa.repository.JpaRepository
import org.springframework.data.jpa.repository.Query

interface ProductRepository : JpaRepository<Product, Int> {

    fun findProductsByCategory(category: Category): List<Product>
    
    @Query("SELECT p FROM Product p WHERE p.quantity <= p.seuilStock")
    fun findByQuantityLessThanEqualSeuilStock(): List<Product>
    
    fun findByNomContainingIgnoreCase(nom: String): List<Product>
}