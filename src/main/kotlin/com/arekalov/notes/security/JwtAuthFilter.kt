package com.arekalov.notes.security

import jakarta.servlet.FilterChain
import jakarta.servlet.http.HttpServletRequest
import jakarta.servlet.http.HttpServletResponse
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken
import org.springframework.security.core.authority.SimpleGrantedAuthority
import org.springframework.security.core.context.SecurityContextHolder
import org.springframework.stereotype.Component
import org.springframework.web.filter.OncePerRequestFilter

private const val AUTHORIZATION_HEADER = "Authorization"

@Component
class JwtAuthFilter(
    private val jwtService: JwtService,
): OncePerRequestFilter() {
    override fun doFilterInternal(
        request: HttpServletRequest,
        response: HttpServletResponse,
        filterChain: FilterChain
    ) {
        val headerValue = request.getHeader(AUTHORIZATION_HEADER)
        if (headerValue != null && headerValue.startsWith(BEARER_PREFIX)) {
            if (jwtService.validateAccessToken(headerValue)) {
                val userId = jwtService.getUserIdFromToken(headerValue)
                
                val authorities = listOf(SimpleGrantedAuthority("ROLE_USER"))
                val auth = UsernamePasswordAuthenticationToken(userId, null, authorities)
                
                SecurityContextHolder.getContext().authentication = auth
            }
        }

        filterChain.doFilter(request, response)
    }
}