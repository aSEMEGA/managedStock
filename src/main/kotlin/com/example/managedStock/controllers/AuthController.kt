package com.example.managedStock.controllers

import com.example.managedStock.dto.UsersDto
import com.example.managedStock.payload.AuthRequest
import com.example.managedStock.payload.TokenResponse
import com.example.managedStock.security.jwt.JwtUtil
import com.example.managedStock.services.AuthService
import jakarta.validation.Valid
import org.springframework.http.HttpStatus
import org.springframework.http.ResponseEntity
import org.springframework.security.authentication.AuthenticationManager
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val authService: AuthService,
                     private val authenticationManager: AuthenticationManager,
                     private val jwtTokenService: JwtUtil
) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody usersDto: UsersDto): ResponseEntity<UsersDto> =
        ResponseEntity.status(201).body(authService.registerUser(usersDto))

    @PostMapping("/login")
    fun login(@RequestBody authRequest: AuthRequest): ResponseEntity<TokenResponse> {
        println("####1 $authRequest")

        val authentication = authenticationManager.authenticate(
            UsernamePasswordAuthenticationToken(authRequest.username, authRequest.password)
        )
        println("aut : $authentication")

        SecurityContextHolder.getContext().authentication = authentication

        val accessToken = jwtTokenService.generateToken(authentication)
        val refreshToken = jwtTokenService.generateRefreshToken(authRequest.username)
        println("#### $accessToken $refreshToken")

        val tokenResponse = TokenResponse(
            accessToken,
            3_600_000,
            604_800_000,
            refreshToken,
            "Bearer",
            accessToken
        )

        return ResponseEntity(tokenResponse, HttpStatus.OK)
    }

    @PostMapping("/vendeur")
    fun createVendeur(@Valid @RequestBody usersDto: UsersDto): ResponseEntity<UsersDto> =
        ResponseEntity.status(201).body(authService.createVendeur(usersDto))

    @GetMapping("/users")
    fun getAllUsers(): ResponseEntity<List<UsersDto>> =
        ResponseEntity.ok(authService.getAllUsers())

    @GetMapping("/users/{id}")
    fun getUserById(@PathVariable id: Int): ResponseEntity<UsersDto> =
        ResponseEntity.ok(authService.getUserById(id))

    @PutMapping("/users/{id}")
    fun updateUser(@PathVariable id: Int, @Valid @RequestBody usersDto: UsersDto): ResponseEntity<UsersDto> =
        ResponseEntity.ok(authService.updateUser(id, usersDto))

    @DeleteMapping("/users/{id}")
    fun deleteUser(@PathVariable id: Int): ResponseEntity<Void> {
        authService.deleteUser(id)
        return ResponseEntity.noContent().build()
    }

    @PostMapping("/change-password")
    fun changePassword(@RequestBody request: Map<String, String>): ResponseEntity<UsersDto> {
        val userId = request["userId"]?.toInt() ?: throw IllegalArgumentException("User ID requis")
        val oldPassword = request["oldPassword"] ?: throw IllegalArgumentException("Ancien mot de passe requis")
        val newPassword = request["newPassword"] ?: throw IllegalArgumentException("Nouveau mot de passe requis")
        
        return ResponseEntity.ok(authService.changePassword(userId, oldPassword, newPassword))
    }
}
