package com.example.managedStock.repository

import com.example.managedStock.dto.CategoryDto
import com.example.managedStock.dto.ProductDto
import com.example.managedStock.entities.Product
import org.springframework.data.jpa.repository.JpaRepository

interface ProductRepository : JpaRepository<Product, Int> {

    fun  findProductsByCategory(category: Category): List<ProductDto>
}