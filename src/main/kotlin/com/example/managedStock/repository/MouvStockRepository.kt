package com.example.managedStock.repository

import com.example.managedStock.entities.MouvStock
import com.example.managedStock.entities.Product
import org.springframework.data.jpa.repository.JpaRepository

interface MouvStockRepository : JpaRepository<MouvStock, Int> {

    fun findByProductId(productId: Int): List<MouvStock>
    
    fun findByProduct(product: Product): List<MouvStock>
}