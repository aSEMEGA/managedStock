package com.example.managedStock.services

import com.example.managedStock.dto.CategoryDto
import com.example.managedStock.enums.State
import com.example.managedStock.exception.BadRequestException
import com.example.managedStock.exception.NotFoundException
import com.example.managedStock.mappers.CategoryMapper
import com.example.managedStock.repository.CategoryRepository
import org.springframework.stereotype.Service
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private

@Service
class CategoryService(
    private val categoryRepository: CategoryRepository,
    private val categoryMapper: CategoryMapper
) {

    fun createCategory(categoryDto: CategoryDto): CategoryDto {
        val  category = categoryRepository.findById(categoryDto.id);
        if (category != null) {
            throw BadRequestException("Category exists déjà")
        }
        val categoryEntity = categoryMapper.toEntity(categoryDto)
        val savedCategory = categoryRepository.save(categoryEntity)
        return categoryMapper.toDto(savedCategory)

    }

    fun updateCategory(categoryDto: CategoryDto): CategoryDto {
        val existingCategory = categoryRepository.findById(categoryDto.id).orElseThrow{
            NotFoundException("Category non Trouver")
        }

        existingCategory.libelle = categoryDto.libelle

        val categoryEntity = categoryMapper.toEntity(categoryDto)
        val savedCategory = categoryRepository.save(categoryEntity)
        return categoryMapper.toDto(savedCategory)

    }

    fun getAllCategories(): List<CategoryDto> {
        return categoryRepository.findAll().map { categoryMapper.toDto(it) }
    }

    fun getCategoryById(id: Long): CategoryDto {
        val category = categoryRepository.findById(id).orElseThrow{
            NotFoundException("Category non Trouver")
        }
        return categoryMapper.toDto(category)
    }

    fun deleteCategory(id: Long) {
        val category = categoryRepository.findById(id).orElseThrow{
            NotFoundException("Category non Trouver")
        }
        categoryRepository.delete(category)
    }

    fun toggleCategory(id: Long) {
        val category = categoryRepository.findById(id).orElseThrow{
            NotFoundException("Category non Trouver")
        }
        category.isActive = (if (category.isActive == State.ACTIVATED) State.DEACTIVATED else State.ACTIVATED)
        categoryRepository.save(category)
    }
}