package com.example.managedStock.entities

import com.example.managedStock.enums.TypeMouvement
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class MouvStock (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,

    @ManyToOne
    var product: Product,

    @Enumerated(EnumType.STRING)
    var typeMouvement: TypeMouvement,

    var quantity: Int,
    var date: LocalDateTime = LocalDateTime.now()
)