package com.example.managedStock.entities

import com.example.managedStock.enums.State
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Product (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,
    var nom: String = "",
    var quantity: Int = 0,
    var price: Int = 0,
    var imagePath: String? = null,
    @ManyToOne
    var category: Category,
    var seuilStock: Int = 0,
    @Enumerated(EnumType.STRING)
    var isActive: State = State.ACTIVATED,
    var dateCreation: LocalDateTime = LocalDateTime.now()
)