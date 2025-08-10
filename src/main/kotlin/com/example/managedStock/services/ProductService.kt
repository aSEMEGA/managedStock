package com.example.managedStock.services

import com.example.managedStock.dto.MouvStockDto
import com.example.managedStock.dto.ProductDto
import com.example.managedStock.entities.MouvStock
import com.example.managedStock.enums.State
import com.example.managedStock.enums.TypeMouvement
import com.example.managedStock.exception.ConflictException
import com.example.managedStock.exception.NotFoundException
import com.example.managedStock.mappers.ProductMapper
import com.example.managedStock.repository.CategoryRepository
import com.example.managedStock.repository.MouvStockRepository
import com.example.managedStock.repository.ProductRepository
import lombok.RequiredArgsConstructor
import org.springframework.stereotype.Service
import org.springframework.web.multipart.MultipartFile
import java.nio.file.Files
import java.nio.file.Paths
import java.nio.file.StandardCopyOption
import java.time.LocalDateTime
import java.util.*
import kotlin.reflect.jvm.internal.impl.descriptors.Visibilities.Private

@Service
class ProductService (
    private val productRepository: ProductRepository,
    private val categoryRepository: CategoryRepository,
    private val productMapper: ProductMapper,
    private val mouvStockRepository: MouvStockRepository
) {


    fun saveImage(image: MultipartFile): String {
        val uploadDir = Paths.get("uploads")
        if (!Files.exists(uploadDir)) {
            Files.createDirectories(uploadDir)
        }

        val fileName = UUID.randomUUID().toString() + "_" + image.originalFilename
        val filePath = uploadDir.resolve(fileName)

        Files.copy(image.inputStream, filePath, StandardCopyOption.REPLACE_EXISTING)

        return filePath.toString()
    }

    fun createProduct(productDto: ProductDto, image : MultipartFile?): ProductDto {
        val category = categoryRepository.findById(productDto.categoryId).orElseThrow {
            NotFoundException("Category non Trouver") }
        if (productRepository.existsById(productDto.id)) {
            throw ConflictException("Produit avec ID ${productDto.id} existe déjà")
        }

        val imagePath = image?.let { saveImage(it) }
        val productMap = productMapper.toEntity(productDto, category)
        productMap.imagePath = imagePath
        val savedProduct = productRepository.save(productMap)
        val mouvStock = MouvStock(
            product = savedProduct,
            quantity = productDto.quantity,
            typeMouvement = TypeMouvement.ENTREE
        )
        mouvStockRepository.save(mouvStock)
        return productMapper.toDto(savedProduct)
    }

    fun updateProduct(productDto: ProductDto, image: MultipartFile?): ProductDto {
        val existingProduct = productRepository.findById(productDto.id).orElseThrow {
            NotFoundException("Produit non Trouver") }

        val category = categoryRepository.findById(productDto.categoryId).orElseThrow {
            NotFoundException("Categorie non Trouver") }

        existingProduct.nom = productDto.nom
        existingProduct.quantity = productDto.quantity
        existingProduct.price = productDto.price
        existingProduct.category = category
        existingProduct.seuilStock = productDto.seuilStock
        existingProduct.isActive = productDto.isActive
        existingProduct.dateCreation = productDto.dateCreation

        image?.let { newImage ->
            if (!newImage.isEmpty) {
                existingProduct.imagePath?.let { oldImagePath ->
                    Files.deleteIfExists(Paths.get(oldImagePath))
                }

                val imagePath = saveImage(newImage)
                existingProduct.imagePath = imagePath
            }
        }

        return productMapper.toDto(productRepository.save(productMapper.toEntity(productDto, existingProduct.category)))
    }

    fun deleteProduct(id: Int) {
        val product = productRepository.findById(id).orElseThrow {
            NotFoundException("Produit non Trouver") }
        productRepository.delete(product)
    }

    fun getAllProducts(): List<ProductDto> {
        return productRepository.findAll().map { productMapper.toDto(it) }
    }

    fun getProductById(id: Int): ProductDto {
        val product = productRepository.findById(id).orElseThrow {
            NotFoundException("Produit non Trouver") }
        return productMapper.toDto(product)
    }

    fun toggleProductActivation(id: Int): ProductDto {
        val product = productRepository.findById(id).orElseThrow {
            NotFoundException("Produit non trouvé")
        }

        product.isActive = if (product.isActive == State.ACTIVATED) State.DEACTIVATED else State.ACTIVATED

        return productMapper.toDto(productRepository.save(product))
    }

    fun searchProducts(query: String): List<ProductDto> {
        return productRepository.findAll().asSequence()
            .filter { it.nom.contains(query) }
            .map { productMapper.toDto(it) }
            .toList()
    }





}