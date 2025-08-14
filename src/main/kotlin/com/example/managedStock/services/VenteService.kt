package com.example.managedStock.services

import com.example.managedStock.dto.ProductDto
import com.example.managedStock.dto.VenteDto
import com.example.managedStock.entities.Product
import com.example.managedStock.entities.Vente
import com.example.managedStock.entities.MouvStock
import com.example.managedStock.repository.ProductRepository
import com.example.managedStock.repository.VenteRepository
import com.example.managedStock.repository.MouvStockRepository
import com.example.managedStock.exception.NotFoundException
import com.example.managedStock.exception.BadRequestException
import com.example.managedStock.enums.TypeVente
import com.example.managedStock.enums.TypeMouvement
import java.time.LocalDateTime
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VenteService(
    private val venteRepository: VenteRepository,
    private val productRepository: ProductRepository,
    private val mouvStockRepository: MouvStockRepository
) {

    @Transactional
    fun createVente(venteDto: VenteDto): VenteDto {
        // Find the product in database
        val existingProduct = productRepository.findById(venteDto.productId).orElseThrow{
            NotFoundException("Product non Trouver")
        }

        // Check if there's sufficient stock
        if(existingProduct.quantity < venteDto.quantite){
            throw BadRequestException("Stock Insuffisant")
        }

        // Calculate prices
        val prixUnitaire = if (venteDto.prixUnitaire > 0) venteDto.prixUnitaire else existingProduct.price
        val totalSansReduction = prixUnitaire * venteDto.quantite
        val totalAvecReduction = totalSansReduction - venteDto.reduction

        // Update product quantity
        existingProduct.quantity -= venteDto.quantite
        productRepository.save(existingProduct)

        // Create vente entity (simplified for now)
        val vente = Vente(
            id = 0,
            datecreationVente = LocalDateTime.now(),
            quantite = venteDto.quantite,
            prixUnitaire = prixUnitaire,
            total = totalAvecReduction,
            reduction = venteDto.reduction,
            montantPaye = venteDto.montantPaye,
            montantCredit = venteDto.montantCredit,
            dateEcheance = venteDto.dateEcheance,
            isCredit = venteDto.isCredit,
            isPaye = venteDto.isPaye,
            product = existingProduct,
            client = null, // Will be added later
            vendeur = null, // Will be added later
            typeVente = venteDto.TypeVente,
            typeMouvement = TypeMouvement.SORTIE
        )

        val savedVente = venteRepository.save(vente)

        // Create stock movement
        val mouvStock = MouvStock(
            product = existingProduct,
            quantity = venteDto.quantite,
            typeMouvement = TypeMouvement.SORTIE
        )
        mouvStockRepository.save(mouvStock)

        return VenteDto(
            id = savedVente.id,
            datecreationVente = savedVente.datecreationVente,
            quantite = savedVente.quantite,
            prixUnitaire = savedVente.prixUnitaire,
            total = savedVente.total,
            reduction = savedVente.reduction,
            montantPaye = savedVente.montantPaye,
            montantCredit = savedVente.montantCredit,
            dateEcheance = savedVente.dateEcheance,
            isCredit = savedVente.isCredit,
            isPaye = savedVente.isPaye,
            productId = savedVente.product?.id ?: 0,
            clientId = savedVente.client?.id,
            vendeurId = savedVente.vendeur?.id ?: 0,
            TypeVente = savedVente.typeVente ?: TypeVente.DIRECT
        )
    }

    fun getAll(): List<VenteDto> {
        return venteRepository.findAll().map { vente ->
            VenteDto(
                id = vente.id,
                datecreationVente = vente.datecreationVente,
                quantite = vente.quantite,
                prixUnitaire = vente.prixUnitaire,
                total = vente.total,
                reduction = vente.reduction,
                montantPaye = vente.montantPaye,
                montantCredit = vente.montantCredit,
                dateEcheance = vente.dateEcheance,
                isCredit = vente.isCredit,
                isPaye = vente.isPaye,
                productId = vente.product?.id ?: 0,
                clientId = vente.client?.id,
                vendeurId = vente.vendeur?.id ?: 0,
                TypeVente = vente.typeVente ?: TypeVente.DIRECT
            )
        }
    }

    fun getVenteById(id: Int): VenteDto {
        val vente = venteRepository.findById(id).orElseThrow{
            NotFoundException("Vente non Trouver")
        }
        return VenteDto(
            id = vente.id,
            datecreationVente = vente.datecreationVente,
            quantite = vente.quantite,
            prixUnitaire = vente.prixUnitaire,
            total = vente.total,
            reduction = vente.reduction,
            montantPaye = vente.montantPaye,
            montantCredit = vente.montantCredit,
            dateEcheance = vente.dateEcheance,
            isCredit = vente.isCredit,
            isPaye = vente.isPaye,
            productId = vente.product?.id ?: 0,
            clientId = vente.client?.id,
            vendeurId = vente.vendeur?.id ?: 0,
            TypeVente = vente.typeVente ?: TypeVente.DIRECT
        )
    }

    fun getVentesByProduct(productId: Int): List<VenteDto> {
        val product = productRepository.findById(productId).orElseThrow{
            NotFoundException("Product non Trouver")
        }
        return venteRepository.findByProduct(product).map { vente ->
            VenteDto(
                id = vente.id,
                datecreationVente = vente.datecreationVente,
                quantite = vente.quantite,
                prixUnitaire = vente.prixUnitaire,
                total = vente.total,
                reduction = vente.reduction,
                montantPaye = vente.montantPaye,
                montantCredit = vente.montantCredit,
                dateEcheance = vente.dateEcheance,
                isCredit = vente.isCredit,
                isPaye = vente.isPaye,
                productId = vente.product?.id ?: 0,
                clientId = vente.client?.id,
                vendeurId = vente.vendeur?.id ?: 0,
                TypeVente = vente.typeVente ?: TypeVente.DIRECT
            )
        }
    }

    fun getVentesByDateRange(startDate: LocalDateTime, endDate: LocalDateTime): List<VenteDto> {
        return venteRepository.findByDatecreationVenteBetween(startDate, endDate).map { vente ->
            VenteDto(
                id = vente.id,
                datecreationVente = vente.datecreationVente,
                quantite = vente.quantite,
                prixUnitaire = vente.prixUnitaire,
                total = vente.total,
                reduction = vente.reduction,
                montantPaye = vente.montantPaye,
                montantCredit = vente.montantCredit,
                dateEcheance = vente.dateEcheance,
                isCredit = vente.isCredit,
                isPaye = vente.isPaye,
                productId = vente.product?.id ?: 0,
                clientId = vente.client?.id,
                vendeurId = vente.vendeur?.id ?: 0,
                TypeVente = vente.typeVente ?: TypeVente.DIRECT
            )
        }
    }

    fun getTotalRevenue(startDate: LocalDateTime, endDate: LocalDateTime): Int {
        return venteRepository.findByDatecreationVenteBetween(startDate, endDate)
            .sumOf { it.total }
    }

    fun getMostSoldProducts(limit: Int = 10): List<Map<String, Any>> {
        return venteRepository.findMostSoldProducts(limit)
    }

    fun getLowStockProducts(): List<ProductDto> {
        return productRepository.findByQuantityLessThanEqualSeuilStock().map { product ->
            ProductDto(
                id = product.id,
                nom = product.nom,
                quantity = product.quantity,
                price = product.price,
                imagePath = product.imagePath,
                categoryId = product.category.id,
                seuilStock = product.seuilStock,
                isActive = product.isActive,
                dateCreation = product.dateCreation
            )
        }
    }

    // TODO: Implement credit functionality when repositories are ready
    fun getVentesEnCredit(): List<VenteDto> {
        return emptyList() // TODO: Implement when ClientRepository is ready
    }

    fun getVentesEnCreditParClient(clientId: Int): List<VenteDto> {
        return emptyList() // TODO: Implement when ClientRepository is ready
    }

    fun payerCredit(venteId: Int, montantPaye: Int): VenteDto {
        throw NotImplementedError("Credit payment not yet implemented")
    }

    fun envoyerRappelsCredit() {
        // TODO: Implement when EmailService is ready
    }

    fun getTotalCreditEnAttente(): Int {
        return 0 // TODO: Implement when repositories are ready
    }
}
}
}