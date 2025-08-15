package com.example.managedStock.services

import com.example.managedStock.dto.ProductDto
import com.example.managedStock.dto.VenteDto
import com.example.managedStock.entities.Client
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
import com.example.managedStock.mappers.VenteMapper
import com.example.managedStock.repository.ClientRepository
import java.time.LocalDateTime
import org.springframework.stereotype.Service
import org.springframework.transaction.annotation.Transactional

@Service
class VenteService(
    private val venteRepository: VenteRepository,
    private val productRepository: ProductRepository,
    private val mouvStockRepository: MouvStockRepository,
    private val venteMapper: VenteMapper,
    private val clientRepository: ClientRepository
) {

    @Transactional
    fun createVente(venteDto: VenteDto): VenteDto {
        val existingProduct = productRepository.findById(venteDto.productId)
            .orElseThrow { NotFoundException("Produit non trouvé") }

        if (venteDto.quantite <= 0) throw BadRequestException("La quantité doit être supérieure à 0")
        if (existingProduct.quantity < venteDto.quantite) throw BadRequestException("Stock insuffisant")

        val prixUnitaire = if (venteDto.prixUnitaire > 0) venteDto.prixUnitaire else existingProduct.price
        val totalSansReduction = prixUnitaire * venteDto.quantite
        if (venteDto.reduction < 0 || venteDto.reduction > totalSansReduction) throw BadRequestException("Réduction invalide")
        val totalAvecReduction = totalSansReduction - venteDto.reduction
        if ((venteDto.montantPaye + venteDto.montantCredit) != totalAvecReduction)
            throw BadRequestException("Montant payé + crédit doit correspondre au total")


        var client: Client? = null
        if (!venteDto.clientNom.isNullOrBlank() && !venteDto.clientTelephone.isNullOrBlank()) {
            client = clientRepository.findByNomAndTelephone(venteDto.clientNom, venteDto.clientTelephone)
                ?: clientRepository.save(
                    Client(
                        nom = venteDto.clientNom,
                        telephone = venteDto.clientTelephone,
                        email = venteDto.clientEmail ?: "",
                        adresse = venteDto.clientAdresse ?: ""
                    )
                )
        }

        existingProduct.quantity -= venteDto.quantite
        productRepository.save(existingProduct)

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
            client = client,
            vendeur = null,
            typeVente = venteDto.typeVente ?: TypeVente.DIRECT,
            typeMouvement = TypeMouvement.SORTIE
        )

        val savedVente = venteRepository.save(vente)

        mouvStockRepository.save(
            MouvStock(
                product = existingProduct,
                quantity = venteDto.quantite,
                typeMouvement = TypeMouvement.SORTIE
            )
        )

        return venteMapper.toDto(savedVente)
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
                clientNom = vente.client?.nom ?: "",
                clientTelephone = vente.client?.telephone ?: "",
                typeVente = vente.typeVente ?: TypeVente.DIRECT,
                typeMouvement = vente.typeMouvement ?: TypeMouvement.SORTIE
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
            clientNom = vente.client?.nom ?: "",
            clientTelephone = vente.client?.telephone ?: "",
            typeVente = vente.typeVente ?: TypeVente.DIRECT,
            typeMouvement = vente.typeMouvement ?: TypeMouvement.SORTIE
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
                clientNom = vente.client?.nom ?: "",
                clientTelephone = vente.client?.telephone ?: "",
                typeVente = vente.typeVente ?: TypeVente.DIRECT,
                typeMouvement = vente.typeMouvement ?: TypeMouvement.SORTIE
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
                clientNom = vente.client?.nom ?: "",
                clientTelephone = vente.client?.telephone ?: "",
                typeVente = vente.typeVente ?: TypeVente.DIRECT,
                typeMouvement = vente.typeMouvement ?: TypeMouvement.SORTIE
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

    fun getVentesEnCredit(): List<VenteDto> {
        return venteRepository.findByIsCreditTrueAndIsPayeFalse().map { vente ->
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
                clientNom = vente.client?.nom ?: "",
                clientTelephone = vente.client?.telephone ?: "",
                typeVente = vente.typeVente ?: TypeVente.DIRECT,
                typeMouvement = vente.typeMouvement ?: TypeMouvement.SORTIE
            )
        }
    }

    fun getVentesEnCreditParClient(clientId: Int): List<VenteDto> {
        return venteRepository.findByClientIdAndIsCreditTrueAndIsPayeFalse(clientId).map { vente ->
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
                clientNom = vente.client?.nom ?: "",
                clientTelephone = vente.client?.telephone ?: "",
                typeVente = vente.typeVente ?: TypeVente.DIRECT,
                typeMouvement = vente.typeMouvement ?: TypeMouvement.SORTIE
            )
        }
    }


    fun payerCredit(venteId: Int, montantPaye: Int): VenteDto {
        var vente = venteRepository.findById(venteId).orElseThrow{
            NotFoundException("Vente non Trouver")
        }
       if(!vente.isCredit){
           throw BadRequestException("La vente n'est pas en crédit")
       }
        if(vente.montantCredit < montantPaye){
            throw BadRequestException("Le montant payé est supérieur au montant du crédit")
        }
        if (vente.isPaye) {
            throw BadRequestException("La vente est déjà payée")
        }

        vente.montantPaye += montantPaye
        vente.montantCredit = (vente.total - vente.montantPaye).coerceAtLeast(0)
        vente.isPaye = true
        venteRepository.save(vente)
        return venteMapper.toDto(vente)
    }

    fun envoyerRappelsCredit() {
        // TODO: Implement when EmailService is ready
    }

    fun getTotalCreditEnAttente(): Int {
        return venteRepository.findByIsCreditTrueAndIsPayeFalse().sumOf { it.montantCredit }
    }
}

