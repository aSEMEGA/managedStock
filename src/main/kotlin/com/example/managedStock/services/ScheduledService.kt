package com.example.managedStock.services

import org.springframework.scheduling.annotation.Scheduled
import org.springframework.stereotype.Service

@Service
class ScheduledService(
    private val venteService: VenteService,
    private val emailService: EmailService
) {
    
    // Envoyer les rappels de crédit tous les jours à 9h00
    @Scheduled(cron = "0 0 9 * * ?")
    fun envoyerRappelsCreditQuotidien() {
        try {
            venteService.envoyerRappelsCredit()
            println("Rappels de crédit envoyés automatiquement")
        } catch (e: Exception) {
            println("Erreur lors de l'envoi des rappels de crédit: ${e.message}")
        }
    }
    
    // Vérifier les crédits en retard tous les jours à 10h00
    @Scheduled(cron = "0 0 10 * * ?")
    fun verifierCreditsEnRetard() {
        try {
            val totalCredit = venteService.getTotalCreditEnAttente()
            if (totalCredit > 0) {
                println("Total crédit en attente: $totalCredit FCFA")
            }
        } catch (e: Exception) {
            println("Erreur lors de la vérification des crédits: ${e.message}")
        }
    }
}
