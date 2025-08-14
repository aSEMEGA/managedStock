package com.example.managedStock.controllers

import com.example.managedStock.dto.ProductDto
import com.example.managedStock.services.ProductService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.DeleteMapping
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.PatchMapping
import org.springframework.web.bind.annotation.PathVariable
import org.springframework.web.bind.annotation.PostMapping
import org.springframework.web.bind.annotation.PutMapping
import org.springframework.web.bind.annotation.RequestMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RequestPart
import org.springframework.web.bind.annotation.RestController
import org.springframework.web.multipart.MultipartFile

@RestController
@RequestMapping("/api/v1/products")
class ProductController
    (private val productService: ProductService) {

    @GetMapping
    fun getAll() = ResponseEntity.ok(productService.getAllProducts())

    @GetMapping("/{id}")
    fun getById(@PathVariable id: Int) = ResponseEntity.ok(productService.getProductById(id))

    @PostMapping(consumes = ["multipart/form-data"])
    fun create(
        @RequestPart("product") @Valid productDto: ProductDto,
        @RequestPart("image", required = false) image: MultipartFile?
    ) = ResponseEntity.status(201).body(productService.createProduct(productDto, image))

    @PutMapping(value = ["/{id}"], consumes = ["multipart/form-data"])
    fun update(
        @PathVariable id: Int,
        @RequestPart("product") @Valid productDto: ProductDto,
        @RequestPart("image", required = false) image: MultipartFile?
    ): ResponseEntity<ProductDto> {
        val dto = productDto.copy(id = id)
        return ResponseEntity.ok(productService.updateProduct(dto, image))
    }

    @DeleteMapping("/{id}")
    fun delete(@PathVariable id: Int): ResponseEntity<Void> {
        productService.deleteProduct(id)
        return ResponseEntity.noContent().build()
    }

    @PatchMapping("/{id}/toggle")
    fun toggle(@PathVariable id: Int): ResponseEntity<ProductDto> {
        return ResponseEntity.ok(productService.toggleProductActivation(id))
    }

    @GetMapping("/search")
    fun search(@RequestParam q: String) = ResponseEntity.ok(productService.searchProducts(q))
}