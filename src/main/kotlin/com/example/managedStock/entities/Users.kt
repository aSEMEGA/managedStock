package com.example.managedStock.entities

import com.example.managedStock.enums.Role
import jakarta.persistence.*
import java.time.LocalDateTime

@Entity
class Users (
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int = 0,
    var username: String = "",
    var password: String = "",
    var email: String = "",
    var role: Role,
    var isFirstLogin: Boolean = true,
    var isActive: Boolean = true,
    var dateCreation: LocalDateTime = LocalDateTime.now()
)