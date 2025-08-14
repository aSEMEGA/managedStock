package com.example.managedStock.services

import org.springframework.beans.factory.annotation.Autowired
import org.springframework.mail.SimpleMailMessage
import org.springframework.mail.javamail.JavaMailSender
import org.springframework.stereotype.Service
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter

@Service
class EmailService(
    @Autowired
    private val mailSender: JavaMailSender
) {
    
    fun sendWelcomeEmail(email: String, username: String, password: String) {
        val message = SimpleMailMessage()
        message.setTo(email)
        message.setSubject("Bienvenue dans le système de gestion de stock")
        message.setText("""
            Bonjour $username,
            
            Votre compte a été créé avec succès.
            
            Vos identifiants de connexion :
            - Nom d'utilisateur : $username
            - Mot de passe : $password
            
            IMPORTANT : Vous devez changer votre mot de passe lors de votre première connexion.
            
            Cordialement,
            L'équipe de gestion de stock
        """.trimIndent())
        
        mailSender.send(message)
    }
    
    fun sendCreditReminderEmail(clientEmail: String, clientName: String, montant: Int, dateEcheance: LocalDateTime) {
        val message = SimpleMailMessage()
        message.setTo(clientEmail)
        message.setSubject("Rappel de paiement - Crédit en attente")
        message.setText("""
            Bonjour $clientName,
            
            Nous vous rappelons que vous avez un crédit en attente de paiement.
            
            Montant restant : $montant FCFA
            Date d'échéance : ${dateEcheance.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}
            
            Merci de régler ce montant dans les plus brefs délais.
            
            Cordialement,
            L'équipe de gestion de stock
        """.trimIndent())
        
        mailSender.send(message)
    }
    
    fun sendCreditReminderToAdmin(adminEmail: String, clientName: String, montant: Int, dateEcheance: LocalDateTime) {
        val message = SimpleMailMessage()
        message.setTo(adminEmail)
        message.setSubject("Rappel de crédit - Client $clientName")
        message.setText("""
            Bonjour,
            
            Le client $clientName a un crédit en attente de paiement.
            
            Montant restant : $montant FCFA
            Date d'échéance : ${dateEcheance.format(DateTimeFormatter.ofPattern("dd/MM/yyyy"))}
            
            Veuillez prendre les mesures nécessaires.
            
            Cordialement,
            Système de gestion de stock
        """.trimIndent())
        
        mailSender.send(message)
    }
    
    fun sendPasswordChangeEmail(email: String, username: String) {
        val message = SimpleMailMessage()
        message.setTo(email)
        message.setSubject("Changement de mot de passe")
        message.setText("""
            Bonjour $username,
            
            Votre mot de passe a été modifié avec succès.
            
            Si vous n'êtes pas à l'origine de cette modification, veuillez contacter l'administrateur.
            
            Cordialement,
            L'équipe de gestion de stock
        """.trimIndent())
        
        mailSender.send(message)
    }
}
