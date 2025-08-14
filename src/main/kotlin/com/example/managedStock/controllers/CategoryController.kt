package com.example.managedStock.controllers

import com.example.managedStock.dto.CategoryDto
import com.example.managedStock.services.CategoryService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.validation.annotation.Validated
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestBody
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RestController

@RestController
@RequestMapping("/api/v1/categories")
@Validated
class CategoryController(
    private val categoryService: CategoryService
) {

    @PostMapping
    fun createCategory(categoryDto: CategoryDto) : ResponseEntity<CategoryDto>{

        val create = categoryService.createCategory(categoryDto)
        return ResponseEntity.status(201).body(create)
    }

    @PutMapping("/{id}")
    fun updateCategory(@PathVariable id: Long, @Valid @RequestBody categoryDto: CategoryDto) : ResponseEntity<CategoryDto>{

        val dtoToUpdate  = categoryDto.copy(id = id)
        val update = categoryService.updateCategory(dtoToUpdate)
        return ResponseEntity.status(200).body(update)
    }

    @GetMapping
    fun getAllCategories(): ResponseEntity<List<CategoryDto>> {
        val list = categoryService.getAllCategories()
        return ResponseEntity.ok(list)
    }

    @GetMapping("/{id}")
    fun getCategoryById(@PathVariable id: Long): ResponseEntity<CategoryDto> {
        val dto = categoryService.getCategoryById(id)
        return ResponseEntity.ok(dto)
    }

    @DeleteMapping("/{id}")
    fun deleteCategory(@PathVariable id: Long): ResponseEntity<Void> {
        categoryService.deleteCategory(id)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{id}/toggle")
    fun toggleCategory(@PathVariable id: Long): ResponseEntity<Void> {
        categoryService.toggleCategory(id)
        return ResponseEntity.noContent().build()
    }
}