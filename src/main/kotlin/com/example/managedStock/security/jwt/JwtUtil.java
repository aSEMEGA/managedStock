package com.example.managedStock.security.jwt;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.exceptions.JWTVerificationException;
import com.auth0.jwt.interfaces.DecodedJWT;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.util.Date;
import java.util.function.Function;

import static com.example.managedStock.security.config.SecurityConstants.*;


@Component
public class JwtUtil {

    private static final Logger log = LoggerFactory.getLogger(JwtUtil.class);


    // Génération du token JWT
    public String generateToken(Authentication authentication) {
        String login = ((UserDetails) authentication.getPrincipal()).getUsername();
        log.debug("Génération du token pour le nom d'utilisateur: {}", login);
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET_KEY);
            String token = JWT.create()
                    .withSubject(login)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + EXPIRATION_TIME))
                    .sign(algorithm);
            log.debug("Token généré avec succès : {}", token);
            return token;
        } catch (Exception e) {
            log.error("Erreur lors de la génération du token : {}", e.getMessage());
            throw e;
        }
    }

    public String generateRefreshToken(String login) {
        try {
            Algorithm algorithm = Algorithm.HMAC256(JWT_SECRET_KEY);
            return JWT.create()
                    .withSubject(login)
                    .withIssuedAt(new Date())
                    .withExpiresAt(new Date(System.currentTimeMillis() + REFRESH_TOKEN_EXPIRATION_TIME))
                    .sign(algorithm);
        } catch (Exception e) {
            log.error("Erreur lors de la génération du refresh token : {}", e.getMessage());
            throw e;
        }
    }

    // Extraction du nom d'utilisateur à partir du token
    public String extractLogin(String token) throws JWTDecodeException {
        log.debug("Extraction du nom d'utilisateur du token : {}", token);
        try {
            final DecodedJWT decodedJWT = JWT.decode(token);
            log.debug("Token décodé avec succès. Extraction du sujet...");
            return decodedJWT.getSubject();
        } catch (JWTDecodeException e) {
            log.error("Échec de l'extraction du nom d'utilisateur : {}", e.getMessage());
            throw e;
        }
    }

    // Extraction de la date d'expiration à partir du token
    public Date extractExpiration(String token) {
        return extractClaim(token, DecodedJWT::getExpiresAt);
    }

    // Extraction d'une réclamation spécifique avec validation du token
    public <T> T extractClaim(String token, Function<DecodedJWT, T> claimsResolver) {
        log.debug("Décodage du token : {}", token);
        try {
            // Décoder le token
            final DecodedJWT decodedJWT = JWT.decode(token);
            log.debug("Token décodé avec succès. Vérification des réclamations...");
            return claimsResolver.apply(decodedJWT);
        } catch (JWTDecodeException e) {
            log.error("Échec du décodage du token : {}", e.getMessage());
            throw e;
        } catch (JWTVerificationException e) {
            log.error("Échec de la vérification du token : {}", e.getMessage());
            throw e;
        }
    }

    // Vérifie si le token est expiré
    private Boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    // Valide le token en comparant l'utilisateur et l'expiration
    public Boolean isTokenValid(String token, UserDetails userDetails) {
        final String login = extractLogin(token);
        return (login.equals(userDetails.getUsername()) && !isTokenExpired(token));
    }
}
