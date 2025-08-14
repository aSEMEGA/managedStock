package com.example.managedStock.entities

import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Client (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,
    
    var nom: String = "",
    var email: String = "",
    var telephone: String = "",
    var adresse: String = "",
    var dateCreation: LocalDateTime = LocalDateTime.now(),
    var isActive: Boolean = true
)
