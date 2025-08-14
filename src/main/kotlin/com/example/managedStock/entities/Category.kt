package com.example.managedStock.entities

import com.example.managedStock.enums.State
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id


@Entity
class Category (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Long = 0,
    var libelle: String = "",
    var isActive: State = State.ACTIVATED
)