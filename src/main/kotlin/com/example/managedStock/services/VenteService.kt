package com.example.managedStock.services

import com.example.managedStock.dto.ProductDto
import com.example.managedStock.dto.VenteDto
import com.example.managedStock.entities.MouvStock
import com.example.managedStock.entities.Product
import com.example.managedStock.enums.TypeMouvement
import com.example.managedStock.exception.BadRequestException
import com.example.managedStock.repository.ProductRepository
import com.example.managedStock.repository.VenteRepository
import com.example.managedStock.exception.NotFoundException
import com.example.managedStock.mappers.VenteMapper
import com.example.managedStock.repository.MouvStockRepository

class VenteService(
    private val venteRepository: VenteRepository,
    private val productRepository: ProductRepository,
    private val mouvStockRepository: MouvStockRepository,
    private val venteMapper: VenteMapper
) {

    fun getAll(): List<VenteDto> = venteRepository.findAll().map { venteMapper.toDto(it) }

    fun createVente(dto: VenteDto) : VenteDto{

        val product = productRepository.findById(dto.productId).orElseThrow {
            NotFoundException("Produit non trouvé")
        }

        if (product.quantity < dto.quantite) {
            throw BadRequestException("Stock insuffisant pour le produit ${product.nom}")
        }

        val totalVente = product.price * dto.quantite

        product.quantity -= dto.quantite
        productRepository.save(product)

        val mouv = MouvStock(product = product, quantity = dto.quantite, typeMouvement = TypeMouvement.SORTIE)
        mouvStockRepository.save(mouv)

        val vente = venteMapper.toEntity(dto, product)
        vente.total = totalVente
        val savedVente = venteRepository.save(vente)
        return venteMapper.toDto(savedVente)
    }
}