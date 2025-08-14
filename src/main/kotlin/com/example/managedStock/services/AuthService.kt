package com.example.managedStock.services

import com.example.managedStock.dto.UsersDto
import com.example.managedStock.entities.Users
import com.example.managedStock.enums.Role
import com.example.managedStock.exception.BadRequestException
import com.example.managedStock.exception.NotFoundException
import com.example.managedStock.repository.UsersRepository
import io.jsonwebtoken.Jwts
import io.jsonwebtoken.SignatureAlgorithm
import io.jsonwebtoken.security.Keys
import org.springframework.security.crypto.password.PasswordEncoder
import org.springframework.stereotype.Service
import java.util.*

@Service
class AuthService(
    private val usersRepository: UsersRepository,
    private val passwordEncoder: PasswordEncoder,
    private val emailService: EmailService
) {

    private val secretKey = Keys.secretKeyFor(SignatureAlgorithm.HS256)

    fun registerUser(usersDto: UsersDto): UsersDto {
        if (usersRepository.existsByUsername(usersDto.username)) {
            throw BadRequestException("Username déjà utilisé")
        }
        
        if (usersRepository.existsByEmail(usersDto.email)) {
            throw BadRequestException("Email déjà utilisé")
        }

        // Generate random password for first login
        val tempPassword = generateRandomPassword()
        val encodedPassword = passwordEncoder.encode(tempPassword)
        
        val user = Users(
            id = 0,
            username = usersDto.username,
            password = encodedPassword,
            email = usersDto.email,
            role = usersDto.role,
            isFirstLogin = true,
            isActive = true,
            dateCreation = LocalDateTime.now()
        )

        val savedUser = usersRepository.save(user)
        
        // Send welcome email with credentials
        emailService.sendWelcomeEmail(savedUser.email, savedUser.username, tempPassword)
        
        return UsersDto(
            id = savedUser.id,
            username = savedUser.username,
            email = savedUser.email,
            role = savedUser.role,
            isFirstLogin = savedUser.isFirstLogin,
            isActive = savedUser.isActive,
            dateCreation = savedUser.dateCreation
        )
    }

    fun login(username: String, password: String): Map<String, Any> {
        val user = usersRepository.findByUsername(username)
            .orElseThrow { BadRequestException("Username ou mot de passe incorrect") }

        if (!passwordEncoder.matches(password, user.password)) {
            throw BadRequestException("Username ou mot de passe incorrect")
        }

        val token = generateToken(user)
        return mapOf(
            "token" to token,
            "role" to user.role.name,
            "username" to user.username,
            "isFirstLogin" to user.isFirstLogin
        )
    }

    fun createVendeur(usersDto: UsersDto): UsersDto {
        // Seul un admin peut créer un vendeur
        val vendeurDto = usersDto.copy(role = Role.VENDEUR)
        return registerUser(vendeurDto)
    }

    fun getAllUsers(): List<UsersDto> {
        return usersRepository.findAll().map { user ->
            UsersDto(
                id = user.id,
                username = user.username,
                email = user.email,
                role = user.role
            )
        }
    }

    fun getUserById(id: Int): UsersDto {
        val user = usersRepository.findById(id).orElseThrow {
            NotFoundException("Utilisateur non trouvé")
        }
        return UsersDto(
            id = user.id,
            username = user.username,
            email = user.email,
            role = user.role
        )
    }

    fun updateUser(id: Int, usersDto: UsersDto): UsersDto {
        val existingUser = usersRepository.findById(id).orElseThrow {
            NotFoundException("Utilisateur non trouvé")
        }

        existingUser.username = usersDto.username
        existingUser.email = usersDto.email
        if (usersDto.password.isNotBlank()) {
            existingUser.password = passwordEncoder.encode(usersDto.password)
        }

        val savedUser = usersRepository.save(existingUser)
        return UsersDto(
            id = savedUser.id,
            username = savedUser.username,
            email = savedUser.email,
            role = savedUser.role
        )
    }

    fun deleteUser(id: Int) {
        if (!usersRepository.existsById(id)) {
            throw NotFoundException("Utilisateur non trouvé")
        }
        usersRepository.deleteById(id)
    }

    private fun generateToken(user: Users): String {
        val claims = mapOf(
            "username" to user.username,
            "role" to user.role.name,
            "userId" to user.id.toString()
        )

        return Jwts.builder()
            .setClaims(claims)
            .setSubject(user.username)
            .setIssuedAt(Date())
            .setExpiration(Date(System.currentTimeMillis() + 86400000)) // 24 heures
            .signWith(secretKey)
            .compact()
    }

    fun validateToken(token: String): Map<String, Any> {
        return try {
            val claims = Jwts.parserBuilder()
                .setSigningKey(secretKey)
                .build()
                .parseClaimsJws(token)
                .body

            mapOf(
                "username" to claims["username"],
                "role" to claims["role"],
                "userId" to claims["userId"]
            )
        } catch (e: Exception) {
            throw BadRequestException("Token invalide")
        }
    }

    fun changePassword(userId: Int, oldPassword: String, newPassword: String): UsersDto {
        val user = usersRepository.findById(userId).orElseThrow {
            NotFoundException("Utilisateur non trouvé")
        }

        if (!passwordEncoder.matches(oldPassword, user.password)) {
            throw BadRequestException("Ancien mot de passe incorrect")
        }

        user.password = passwordEncoder.encode(newPassword)
        user.isFirstLogin = false

        val savedUser = usersRepository.save(user)
        
        // Send password change notification
        emailService.sendPasswordChangeEmail(savedUser.email, savedUser.username)

        return UsersDto(
            id = savedUser.id,
            username = savedUser.username,
            email = savedUser.email,
            role = savedUser.role,
            isFirstLogin = savedUser.isFirstLogin,
            isActive = savedUser.isActive,
            dateCreation = savedUser.dateCreation
        )
    }

    private fun generateRandomPassword(): String {
        val chars = "ABCDEFGHIJKLMNOPQRSTUVWXYZabcdefghijklmnopqrstuvwxyz0123456789"
        return (1..8).map { chars.random() }.joinToString("")
    }
}
