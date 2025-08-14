package com.example.managedStock.mappers

import com.example.managedStock.dto.ClientDto
import com.example.managedStock.entities.Client
import org.springframework.stereotype.Component

@Component
class ClientMapper {
    
    fun toDto(client: Client): ClientDto {
        return ClientDto(
            id = client.id,
            nom = client.nom,
            email = client.email,
            telephone = client.telephone,
            adresse = client.adresse,
            dateCreation = client.dateCreation,
            isActive = client.isActive
        )
    }
    
    fun toEntity(clientDto: ClientDto): Client {
        return Client(
            id = clientDto.id,
            nom = clientDto.nom,
            email = clientDto.email,
            telephone = clientDto.telephone,
            adresse = clientDto.adresse,
            dateCreation = clientDto.dateCreation,
            isActive = clientDto.isActive
        )
    }
}
