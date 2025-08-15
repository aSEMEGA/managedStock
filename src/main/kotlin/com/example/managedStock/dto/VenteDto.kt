package com.example.managedStock.dto

import com.example.managedStock.entities.Product
import com.example.managedStock.enums.TypeMouvement
import com.example.managedStock.enums.TypeVente
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.ManyToOne
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class VenteDto(
    val id: Int = 0,
    val productId: Int,
    var datecreationVente: LocalDateTime = LocalDateTime.now(),
    val quantite: Int,
    val prixUnitaire: Int = 0,
    val total: Int = 0,
    val reduction: Int = 0,
    val montantPaye: Int = 0,
    val montantCredit: Int = 0,
    val dateEcheance: LocalDateTime? = null,
    val isCredit: Boolean = false,
    val isPaye: Boolean = true,
    val typeVente: TypeVente? = TypeVente.DIRECT,
    val clientNom: String? = null,
    val clientTelephone: String? = null,
    val clientEmail: String? = null,
    val clientAdresse: String? = null,
    val typeMouvement: TypeMouvement
)
