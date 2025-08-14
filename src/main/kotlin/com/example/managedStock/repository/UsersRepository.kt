package com.example.managedStock.repository

import com.example.managedStock.entities.Users
import org.springframework.data.jpa.repository.JpaRepository
import java.util.*

interface UsersRepository : JpaRepository<Users, Int> {
    fun findByUsername(username: String): Optional<Users>
    fun findByEmail(email: String): Optional<Users>
    fun existsByUsername(username: String): Boolean
    fun existsByEmail(email: String): Boolean
}
