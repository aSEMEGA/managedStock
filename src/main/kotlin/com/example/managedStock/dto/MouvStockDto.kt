package com.example.managedStock.dto

import com.example.managedStock.enums.TypeMouvement
import java.time.LocalDateTime

data class MouvStockDto (

    var id : Int,
    var product : String,
    var typeMouvement: TypeMouvement,
    var quantity: Int?,
    var date : LocalDateTime = LocalDateTime.now()
)