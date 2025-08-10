package com.example.managedStock.entities

import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id
import java.time.LocalDateTime
@Entity
class LogAction (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,
    var actionName: String?,
    var actionDate: LocalDateTime?,
    var actionUser: String?,
    var commentaire: String?,
    var actionType: String?
)