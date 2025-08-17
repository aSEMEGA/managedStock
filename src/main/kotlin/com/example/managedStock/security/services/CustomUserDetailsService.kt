package com.example.OtpGenerator.security.services

import com.example.managedStock.repository.UsersRepository
import com.example.managedStock.entities.Users
import org.springframework.security.core.userdetails.User
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UserDetailsService
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.springframework.stereotype.Service
import java.util.*

@Service
class CustomUserDetailsService(
    private val userRepository: UsersRepository
) : UserDetailsService {

    override fun loadUserByUsername(username: String): UserDetails {
        val user: Optional<Users> = userRepository.findByUsername(username)
            ?: throw UsernameNotFoundException("Utilisateur $username non trouvé")

        return User(user.get().username, user.get().password, emptyList())

    }
}
