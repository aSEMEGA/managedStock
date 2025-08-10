package com.example.managedStock.mappers

import com.example.managedStock.dto.VenteDto
import com.example.managedStock.entities.Product
import com.example.managedStock.entities.Vente
import org.springframework.stereotype.Component
import java.time.LocalDateTime

@Component
class VenteMapper {
    fun toDto(vente: Vente) = VenteDto(
        id = vente.id,
        datecreationVente = vente.datecreationVente,
        quantite = vente.quantite,
        total = vente.total,
        productId = vente.product.id,
        TypeVente = vente.TypeVente,
        TypeMouvement = vente.TypeMouvement
    )

    fun toEntity(dto: VenteDto, product: Product) = Vente(
        id = dto.id,
        datecreationVente = dto.datecreationVente ?: LocalDateTime.now(),
        quantite = dto.quantite,
        total = dto.total,
        product = product,
        TypeVente = dto.TypeVente,
        TypeMouvement = dto.TypeMouvement
    )
}
