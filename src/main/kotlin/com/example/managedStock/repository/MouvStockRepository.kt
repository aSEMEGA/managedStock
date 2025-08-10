package com.example.managedStock.repository

import com.example.managedStock.entities.MouvStock
import org.springframework.data.jpa.repository.JpaRepository

interface MouvStockRepository : JpaRepository<MouvStock, Long> {
}