package com.example.managedStock.mappers

import com.example.managedStock.dto.CategoryDto
import com.example.managedStock.entities.Category
import org.springframework.stereotype.Component


@Component
class CategoryMapper {

    fun toEntity(categoryDto: CategoryDto): Category {
        return Category(
            id = categoryDto.id,
            libelle = categoryDto.libelle,
            isActive = categoryDto.isActive
        )
    }

    fun toDto(category: Category): CategoryDto {
        return CategoryDto(
            id = category.id,
            libelle = category.libelle,
            isActive = category.isActive
        )
    }
}