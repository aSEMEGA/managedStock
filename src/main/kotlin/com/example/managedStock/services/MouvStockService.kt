package com.example.managedStock.services

import com.example.managedStock.dto.MouvStockDto
import com.example.managedStock.exception.NotFoundException
import com.example.managedStock.mappers.MouvStockMapper
import com.example.managedStock.repository.MouvStockRepository
import com.example.managedStock.repository.ProductRepository
import org.springframework.stereotype.Service

@Service
class MouvStockService(
    private val mouvStockRepository: MouvStockRepository,
    private val mouvStockMapper: MouvStockMapper,
    private val productRepository: ProductRepository
) {
    fun getAll(): List<MouvStockDto> = mouvStockRepository.findAll().map { mouvStockMapper.toDto(it) }
    fun getByProduct(productId: Int): List<MouvStockDto> {
        if (!productRepository.existsById(productId)) {
            throw NotFoundException("Produit avec ID $productId non trouvé")
        }

        return mouvStockRepository.findByProductId(productId)
            .map { mouvStockMapper.toDto(it) }
            .ifEmpty { throw NotFoundException("Aucun mouvement de stock trouvé pour le produit $productId") }

    }
}