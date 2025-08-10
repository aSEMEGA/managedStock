package com.example.managedStock.entities

import com.example.managedStock.enums.Role
import jakarta.persistence.Entity
import jakarta.persistence.GeneratedValue
import jakarta.persistence.GenerationType
import jakarta.persistence.Id

@Entity
class Users (

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id : Int,
    var username : String,
    var password : String,
    var email : String,
    var role : Role
)