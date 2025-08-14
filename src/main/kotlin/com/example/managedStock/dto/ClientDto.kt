package com.example.managedStock.dto

import jakarta.validation.constraints.Email
import jakarta.validation.constraints.NotBlank
import java.time.LocalDateTime

data class ClientDto(
    var id: Int = 0,
    @field:NotBlank(message = "Le nom du client est obligatoire")
    var nom: String = "",
    @field:Email(message = "L'email doit être valide")
    var email: String = "",
    var telephone: String = "",
    var adresse: String = "",
    var dateCreation: LocalDateTime = LocalDateTime.now(),
    var isActive: Boolean = true
)
