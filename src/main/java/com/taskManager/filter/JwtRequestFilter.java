package com.taskManager.filter;

import com.taskManager.utils.JwtUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;

/**
 * Фильтр для обработки JWT токенов в запросах.
 * Этот фильтр проверяет наличие JWT токена в заголовке Authorization
 * и, если токен действителен, устанавливает аутентификацию пользователя в контексте безопасности.
 */
public class JwtRequestFilter extends OncePerRequestFilter {

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private UserDetailsService userDetailsService;

    /**
     * Метод, который выполняется при каждом запросе.
     * Проверяет наличие JWT токена, извлекает имя пользователя и устанавливает аутентификацию,
     * если токен действителен.
     *
     * @param request     - HTTP запрос
     * @param response    - HTTP ответ
     * @param filterChain - цепочка фильтров для обработки запроса
     * @throws ServletException если возникает ошибка при обработке запроса
     * @throws IOException      если возникает ошибка ввода-вывода
     */
    @Override
    protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain)
            throws ServletException, IOException {

        String authorizationHeader = request.getHeader("Authorization");

        if (isTokenPresent(authorizationHeader)) {
            String jwt = authorizationHeader.substring(7);
            String username = jwtUtil.extractUsername(jwt);

            if (username != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                authenticateUser(jwt, username);
            }
        }
        filterChain.doFilter(request, response);
    }

    /**
     * Аутентифицирует пользователя на основе токена и имени пользователя.
     *
     * @param jwt      - JWT токен
     * @param username - имя пользователя
     */
    private void authenticateUser(String jwt, String username) {
        if (jwtUtil.validateToken(jwt, username)) {
            UserDetails userDetails = userDetailsService.loadUserByUsername(username);
            UsernamePasswordAuthenticationToken authToken =
                    new UsernamePasswordAuthenticationToken(userDetails, null, userDetails.getAuthorities());
            SecurityContextHolder.getContext().setAuthentication(authToken);
        }
    }

    /**
     * Проверяет, присутствует ли токен в заголовке Authorization.
     *
     * @param authorizationHeader - заголовок Authorization
     * @return true, если токен присутствует, иначе false
     */
    private boolean isTokenPresent(String authorizationHeader) {
        return authorizationHeader != null && authorizationHeader.startsWith("Bearer ");
    }
}
