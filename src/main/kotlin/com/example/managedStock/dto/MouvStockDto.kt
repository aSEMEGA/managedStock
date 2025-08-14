package com.example.managedStock.dto

import com.example.managedStock.enums.TypeMouvement
import java.time.LocalDateTime

data class MouvStockDto (
    var id: Int = 0,
    var productId: Int,
    var productName: String,
    var typeMouvement: TypeMouvement,
    var quantity: Int,
    var date: LocalDateTime = LocalDateTime.now()
)