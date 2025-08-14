package com.example.managedStock.dto

import com.example.managedStock.enums.State
import jakarta.validation.constraints.NotBlank

data class CategoryDto(
    var id: Long = 0,
    @field:NotBlank(message = "le nom est obligatoire")
    var libelle: String = "",
    var isActive: State = State.ACTIVATED
)
