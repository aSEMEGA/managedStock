package com.example.managedStock.dto

import com.example.managedStock.entities.Product
import com.example.managedStock.enums.TypeMouvement
import com.example.managedStock.enums.TypeVente
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

data class VenteDto (
    var id: Int = 0,
    var datecreationVente: LocalDateTime = LocalDateTime.now(),
    var quantite: Int = 0,
    var prixUnitaire: Int = 0,
    var total: Int = 0,
    var reduction: Int = 0,
    var montantPaye: Int = 0,
    var montantCredit: Int = 0,
    var dateEcheance: LocalDateTime? = null,
    var isCredit: Boolean = false,
    var isPaye: Boolean = true,
    var productId: Int = 0,
    var clientId: Int? = null,
    var vendeurId: Int = 0,
    var TypeVente: TypeVente
)