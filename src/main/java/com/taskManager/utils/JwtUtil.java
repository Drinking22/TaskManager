package com.taskManager.utils;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.JwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import javax.crypto.SecretKey;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

/**
 * Утилитный класс для работы с JSON Web Tokens (JWT).
 * Этот класс предоставляет методы для генерации, валидации и извлечения информации из JWT.
 */

@Component
public class JwtUtil extends BaseLoggerService {

    private static final long VALIDITY_TIME = 900000;

    @Value("${security.jwt.secret-key}")
    private String secretKey;

    /**
     * Генерирует JWT токен для указанного имени пользователя.
     *
     * @param username - имя пользователя, для которого генерируется токен
     * @return сгенерированный JWT токен
     */
    public String generateToken(String username) {
        logger.info("Generating token for user: {}", username);
        Map<String, Object> claims = new HashMap<>();
        return createToken(claims, username);
    }

    /**
     * Создает JWT токен с указанными данными и именем пользователя.
     *
     * @param claims - данные, которые будут включены в токен
     * @param username - имя пользователя, для которого создается токен
     * @return созданный JWT токен
     */
    public String createToken(Map<String, Object> claims, String username) {
        logger.info("Creating token with claims: {} for user: {}", claims, username);
        return Jwts.builder()
                .claims(claims)
                .subject(username)
                .issuedAt(new Date(System.currentTimeMillis()))
                .expiration(new Date(System.currentTimeMillis() + VALIDITY_TIME))
                .signWith(getSignInKey(), Jwts.SIG.HS256)
                .compact();
    }

    /**
     * Проверяет валидность указанного JWT токена по сравнению с заданным именем пользователя.
     *
     * @param token - JWT токен для проверки
     * @param username - имя пользователя для проверки
     * @return true, если токен валиден, иначе false
     */
    public boolean validateToken(String token, String username) {
        logger.info("Validating token for user: {}", username);
        try {
            String extractedUsername = extractUsername(token);
            boolean isValid = (extractedUsername.equals(username) && !isTokenExpired(token));
            logger.info("Token validation result for user {}: {}", username, isValid);
            return isValid;
        } catch (JwtException ex) {
            logger.error("Token validation failed: {}", ex.getMessage());
            return false;
        }
    }

    /**
     * Проверяет, истек ли указанный JWT токен.
     *
     * @param token - JWT токен для проверки
     * @return true, если токен истек, иначе false
     */
    public boolean isTokenExpired(String token) {
        boolean expired = extractAllClaims(token).getExpiration().before(new Date());
        logger.info("Token expired status: {}", expired);
        return expired;
    }

    /**
     * Извлекает имя пользователя из указанного JWT токена.
     *
     * @param token - JWT токен, из которого нужно извлечь имя пользователя
     * @return извлеченное имя пользователя, или null, если извлечение не удалось
     */
    public String extractUsername(String token) {
        try {
            String username = extractAllClaims(token).getSubject();
            logger.info("Extracted username from token: {}", username);
            return username;
        } catch (JwtException ex) {
            logger.error("Failed to extract username from token: {}", ex.getMessage());
            return null;
        }

    }

    /**
     * Извлекает все данные (claims) из указанного JWT токена.
     *
     * @param token - JWT токен, из которого нужно извлечь данные
     * @return извлеченные данные из токена
     * @throws JwtException если токен недействителен или не может быть распарсен
     */
    public Claims extractAllClaims(String token) {
        logger.info("Extracting claims from token");
        return Jwts.parser()
                .setSigningKey(secretKey)
                .build()
                .parseSignedClaims(token)
                .getPayload();
    }

    /**
     * Получает ключ подписи, используемый для подписи JWT.
     *
     * @return SecretKey, используемый для подписи
     */
    private SecretKey getSignInKey() {
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        return Keys.hmacShaKeyFor(keyBytes);
    }
}
