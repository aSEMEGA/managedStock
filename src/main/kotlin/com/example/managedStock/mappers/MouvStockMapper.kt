package com.example.managedStock.mappers

import com.example.managedStock.dto.MouvStockDto
import com.example.managedStock.entities.MouvStock
import org.springframework.stereotype.Component

@Component
class MouvStockMapper {
    
    fun toDto(mouvStock: MouvStock): MouvStockDto {
        return MouvStockDto(
            id = mouvStock.id,
            productId = mouvStock.product.id,
            productName = mouvStock.product.nom,
            typeMouvement = mouvStock.typeMouvement,
            quantity = mouvStock.quantity,
            date = mouvStock.date
        )
    }
    
    fun toEntity(mouvStockDto: MouvStockDto, product: com.example.managedStock.entities.Product): MouvStock {
        return MouvStock(
            id = mouvStockDto.id,
            product = product,
            typeMouvement = mouvStockDto.typeMouvement,
            quantity = mouvStockDto.quantity,
            date = mouvStockDto.date
        )
    }
}
