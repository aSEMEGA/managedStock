package com.example.managedStock.dto

import com.example.managedStock.enums.Role
import jakarta.validation.constraints.NotBlank

data class UsersDto (

    var id : Int,
    @field:NotBlank(message = "le nom d'utilisateur est obligatoire")
    var username : String,
    var password : String,
    var email : String,
    @field:NotBlank(message = "le role ne peut pas être vide ")
    var role : Role
)