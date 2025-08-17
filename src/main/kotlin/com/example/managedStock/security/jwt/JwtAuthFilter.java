package com.example.managedStock.security.jwt;

import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

@Component
public class JwtAuthFilter extends OncePerRequestFilter {

    private final JwtUtil jwtTokenService;
    private final UserDetailsService userDetailsService;

    public JwtAuthFilter(JwtUtil jwtTokenService, UserDetailsService userDetailsService) {
        this.jwtTokenService = jwtTokenService;
        this.userDetailsService = userDetailsService;
    }

    @Override
    protected boolean shouldNotFilter(HttpServletRequest request) {
        String path = request.getServletPath();
        return path.startsWith("/auth/")
                || path.startsWith("/liveness/")
                || path.startsWith("/actuator/")
                || path.startsWith("/swagger-ui/")
                || path.startsWith("/v3/api-docs/");
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {


        String authHeader = request.getHeader("Authorization");


        String token = null;
        String login = null;

        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            token = authHeader.replace("Bearer ", "");

            logger.info("Extracted Token : " + token);

            try {
                login = jwtTokenService.extractLogin(token);
                logger.info("Extracted Username: " + login);
            } catch (JWTDecodeException e) {
                logger.error("Erreur lors de l'extraction du login d'utilisateur : {}");
            }
        }

        // Vérification et authentification de l'utilisateur
        if (login != null && SecurityContextHolder.getContext().getAuthentication() == null) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(login);
            logger.info("UserDetails loaded successfully");

            try {
                if (jwtTokenService.isTokenValid(token, userDetails)) {
                    logger.info("Token is valid");
                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails, null, userDetails.getAuthorities());
                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));
                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    logger.warn("Token is not valid");
                    response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Jeton invalide");
                    return;
                }
            } catch (JWTVerificationException e) {
                logger.error("Erreur lors de la validation du token : {}");
                response.sendError(HttpServletResponse.SC_UNAUTHORIZED, "Erreur lors de la validation du jeton");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }



}
