package com.example.managedStock.mappers

import com.example.managedStock.dto.MouvStockDto
import com.example.managedStock.entities.MouvStock
import org.springframework.stereotype.Component


@Component
class MouvStockMapper {
    fun toDto(mouv: MouvStock) = MouvStockDto(
        id = mouv.id ?: 0,
        product = mouv.product.nom,
        typeMouvement = mouv.typeMouvement,
        quantity = mouv.quantity,
        date = mouv.date
    )
}
