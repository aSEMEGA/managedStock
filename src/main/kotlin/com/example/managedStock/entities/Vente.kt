package com.example.managedStock.entities

import com.example.managedStock.enums.TypeMouvement
import com.example.managedStock.enums.TypeVente
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Vente (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Int,
    var datecreationVente : LocalDateTime,
    var quantite : Int,
    var total : Double,
    @ManyToOne
    var product: Product,
    @Enumerated(EnumType.STRING)
    var TypeVente: TypeVente,
    @Enumerated(EnumType.STRING)
    var TypeMouvement: TypeMouvement
)