package com.example.managedStock.dto

import com.example.managedStock.entities.Category
import com.example.managedStock.enums.State
import jakarta.persistence.EnumType
import jakarta.persistence.Enumerated
import jakarta.persistence.ManyToOne
import jakarta.validation.constraints.Min
import jakarta.validation.constraints.NotBlank
import jakarta.validation.constraints.NotNull
import jakarta.validation.constraints.Positive
import java.time.LocalDateTime

data class ProductDto (

    var id: Int,
    @field:NotBlank(message = "Le nom du produit est obligatoire")
    var nom: String,
    @field:NotNull(message = "La quantité du produit est obligatoire")
    @field:Positive(message = "La quantité du produit doit etre superieur a 0")
    var quantity: Int,
    @field:NotNull(message = "Le prix du produit est obligatoire")
    @field:Positive
    @field:Min(value = 1, message = "Le prix du produit doit etre superieur a 0")
    var price: Int,
    var imagePath: String?,
    @field:NotNull(message = "La catégorie du produit est obligatoire")
    var categoryId: Long,
    @field:NotNull(message = "Le seuil de stock est obligatoire")
    @field:Positive(message = "Le seuil de stock doit etre superieur a 0")
    var seuilStock : Int,
    var isActive: State = State.ACTIVATED,
    var dateCreation: LocalDateTime = LocalDateTime.now(),


    )