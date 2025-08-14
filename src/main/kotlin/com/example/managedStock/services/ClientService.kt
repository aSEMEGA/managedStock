package com.example.managedStock.services

import com.example.managedStock.dto.ClientDto
import com.example.managedStock.exception.NotFoundException
import com.example.managedStock.mappers.ClientMapper
import com.example.managedStock.repository.ClientRepository
import org.springframework.stereotype.Service

@Service
class ClientService(
    private val clientRepository: ClientRepository,
    private val clientMapper: ClientMapper
) {
    
    fun createClient(clientDto: ClientDto): ClientDto {
        val client = clientMapper.toEntity(clientDto)
        val savedClient = clientRepository.save(client)
        return clientMapper.toDto(savedClient)
    }
    
    fun updateClient(id: Int, clientDto: ClientDto): ClientDto {
        val existingClient = clientRepository.findById(id).orElseThrow {
            NotFoundException("Client non trouvé")
        }
        
        existingClient.nom = clientDto.nom
        existingClient.email = clientDto.email
        existingClient.telephone = clientDto.telephone
        existingClient.adresse = clientDto.adresse
        existingClient.isActive = clientDto.isActive
        
        val savedClient = clientRepository.save(existingClient)
        return clientMapper.toDto(savedClient)
    }
    
    fun getAllClients(): List<ClientDto> {
        return clientRepository.findAll().map { clientMapper.toDto(it) }
    }
    
    fun getActiveClients(): List<ClientDto> {
        return clientRepository.findActiveClients().map { clientMapper.toDto(it) }
    }
    
    fun getClientById(id: Int): ClientDto {
        val client = clientRepository.findById(id).orElseThrow {
            NotFoundException("Client non trouvé")
        }
        return clientMapper.toDto(client)
    }
    
    fun deleteClient(id: Int) {
        if (!clientRepository.existsById(id)) {
            throw NotFoundException("Client non trouvé")
        }
        clientRepository.deleteById(id)
    }
    
    fun searchClients(query: String): List<ClientDto> {
        return clientRepository.findByNomContainingIgnoreCase(query).map { clientMapper.toDto(it) }
    }
    
    fun getClientByEmail(email: String): ClientDto? {
        val client = clientRepository.findByEmail(email)
        return client?.let { clientMapper.toDto(it) }
    }
}
