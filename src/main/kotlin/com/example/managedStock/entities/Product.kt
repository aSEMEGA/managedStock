package com.example.managedStock.entities

import com.example.managedStock.enums.State
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Product (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int,
    var nom: String,
    var quantity: Int,
    var price: Int,
    var imagePath: String?,
    @ManyToOne
    var category: Category,
    var seuilStock : Int,
    @Enumerated(EnumType.STRING)
    var isActive: State = State.ACTIVATED,
    var dateCreation: LocalDateTime = LocalDateTime.now(),


)