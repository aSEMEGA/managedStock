package com.example.managedStock.controllers

import com.example.managedStock.dto.UsersDto
import com.example.managedStock.services.AuthService
import jakarta.validation.Valid
import org.springframework.http.ResponseEntity
import org.springframework.web.bind.annotation.*

@RestController
@RequestMapping("/api/v1/auth")
class AuthController(private val authService: AuthService) {

    @PostMapping("/register")
    fun register(@Valid @RequestBody usersDto: UsersDto): ResponseEntity<UsersDto> =
        ResponseEntity.status(201).body(authService.registerUser(usersDto))

    @PostMapping("/login")
    fun login(@RequestBody loginRequest: Map<String, String>): ResponseEntity<Map<String, String>> {
        val username = loginRequest["username"] ?: throw IllegalArgumentException("Username requis")
        val password = loginRequest["password"] ?: throw IllegalArgumentException("Password requis")
        
        return ResponseEntity.ok(authService.login(username, password))
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
