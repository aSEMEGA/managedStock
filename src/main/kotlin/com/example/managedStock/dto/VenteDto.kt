package com.example.managedStock.dto

import com.example.managedStock.entities.Product
import com.example.managedStock.enums.TypeMouvement
import com.example.managedStock.enums.TypeVente
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.ManyToOne
import java.time.LocalDateTime

data class VenteDto (

    var id : Int,
    var datecreationVente : LocalDateTime,
    var quantite : Int,
    var total : Double,
    var productId: Int,
    var TypeVente: TypeVente,
    var TypeMouvement: TypeMouvement
)