package com.example.managedStock.entities

import com.example.managedStock.enums.TypeMouvement
import com.example.managedStock.enums.TypeVente
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Vente (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
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
    
    @ManyToOne
    var product: Product? = null,
    
    @ManyToOne
    var client: Client? = null,
    
    @ManyToOne
    var vendeur: Users? = null,
    
    @Enumerated(EnumType.STRING)
    var typeVente: TypeVente? = null,
    
    @Enumerated(EnumType.STRING)
    var typeMouvement: TypeMouvement? = null
)