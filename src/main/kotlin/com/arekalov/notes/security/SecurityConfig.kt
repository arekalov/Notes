package com.arekalov.notes.security

import jakarta.servlet.DispatcherType
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration
import org.springframework.http.HttpStatus
import org.springframework.security.config.annotation.web.builders.HttpSecurity
import org.springframework.security.config.http.SessionCreationPolicy
import org.springframework.security.web.authentication.HttpStatusEntryPoint
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter

private const val AUTH_PATH = "/auth/**"
private const val SWAGGER_PATH = "/swagger-ui/**"
private const val API_DOCS_PATH = "/v3/api-docs/**"
private const val SWAGGER_HTML = "/swagger-ui.html"

@Configuration
class SecurityConfig(
    private val jwtAuthFilter: JwtAuthFilter,
) {

    @Bean
    fun filterChain(httpSecurity: HttpSecurity) = httpSecurity
        .csrf { it.disable() }
        .sessionManagement { it.sessionCreationPolicy(SessionCreationPolicy.STATELESS) }
        .authorizeHttpRequests { auth ->
            auth
                .requestMatchers(AUTH_PATH)
                .permitAll()
                .requestMatchers(SWAGGER_PATH, API_DOCS_PATH, SWAGGER_HTML)
                .permitAll()
                .dispatcherTypeMatchers(
                    DispatcherType.ERROR,
                    DispatcherType.FORWARD,
                )
                .permitAll()
                .anyRequest()
                .authenticated()
        }
        .exceptionHandling { confiburer ->
            confiburer
                .authenticationEntryPoint(HttpStatusEntryPoint(HttpStatus.UNAUTHORIZED))

        }
        .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter::class.java)
        .build()
}
