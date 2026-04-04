package it.tennis_club.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configurazione di sicurezza per l'applicazione.
 * 
 * CONCETTO: CORS (Cross-Origin Resource Sharing)
 * 
 * Il frontend Next.js gira su http://localhost:3000
 * Il backend Spring Boot gira su http://localhost:8080
 * 
 * I browser BLOCCANO le richieste tra origini diverse per sicurezza.
 * Senza CORS, se il frontend (porta 3000) prova a chiamare GET /api/campi
 * (porta 8080), il browser rifiuta la risposta con un errore CORS.
 * 
 * La soluzione: dire a Spring Boot "fidati delle richieste da localhost:3000".
 * Questo si fa configurando un bean CorsConfigurationSource.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura Spring Security con CORS abilitato.
     * 
     * .cors(cors -> cors.configurationSource(corsConfigurationSource()))
     * dice a Spring Security di usare la nostra configurazione CORS.
     * Senza questa riga, anche con il bean CORS, Spring Security
     * bloccherebbe le richieste preflight OPTIONS.
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // Abilita CORS usando la configurazione definita sotto
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }

    /**
     * Configurazione CORS — Definisce QUALI origini, metodi e header sono permessi.
     * 
     * Quando il browser fa una richiesta cross-origin, invia prima una richiesta
     * "preflight" (metodo OPTIONS) per chiedere al server cosa è permesso.
     * Il server risponde con gli header CORS. Se sono ok, il browser procede.
     * 
     * setAllowedOrigins: quali URL possono chiamare le nostre API
     * setAllowedMethods: quali metodi HTTP (GET, POST, PUT, DELETE)
     * setAllowedHeaders: quali header il frontend può inviare
     * setAllowCredentials: permette l'invio di cookie/token di autenticazione
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        // Permetti solo il frontend Next.js (porta 3000)
        config.setAllowedOrigins(List.of("http://localhost:3000"));

        // Permetti i metodi HTTP standard per le REST API
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));

        // Permetti questi header nelle richieste del frontend
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));

        // Permetti l'invio di credenziali (cookie, header Authorization)
        config.setAllowCredentials(true);

        // Applica questa configurazione SOLO agli endpoint /api/**
        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return source;
    }
}
