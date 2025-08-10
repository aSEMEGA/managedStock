package com.example.managedStock.mappers

import com.example.managedStock.dto.ProductDto
import com.example.managedStock.entities.Category
import com.example.managedStock.entities.Product
import org.springframework.stereotype.Component

@Component
class ProductMapper {

    fun toDto(product: Product): ProductDto {
        return ProductDto(
            id = product.id,
            nom = product.nom,
            price = product.price,
            quantity = product.quantity,
            imagePath = product.imagePath,
            isActive = product.isActive,
            categoryId = product.category.id,
            seuilStock = product.seuilStock,
            dateCreation = product.dateCreation
        )
    }

    fun toEntity(productDto: ProductDto, category: Category): Product {
        return Product(
            id = productDto.id,
            nom = productDto.nom,
            quantity = productDto.quantity,
            price = productDto.price,
            imagePath = productDto.imagePath ?: "",
            category = category,
            seuilStock = productDto.seuilStock,
            isActive = productDto.isActive,
            dateCreation = productDto.dateCreation
        )
    }
}
