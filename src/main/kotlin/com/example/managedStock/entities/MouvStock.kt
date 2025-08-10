package com.example.managedStock.entities

import com.example.managedStock.enums.TypeMouvement
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class MouvStock (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    val id: Int? = null,

    @ManyToOne
    val product: Product,

    @Enumerated(EnumType.STRING)
    val typeMouvement: TypeMouvement,

    val quantity: Int,
    val date: LocalDateTime = LocalDateTime.now(),


)