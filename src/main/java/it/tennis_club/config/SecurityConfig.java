package it.tennis_club.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;

/**
 * Configurazione di sicurezza per l'applicazione.
 * 
 * Definisce il bean BCryptPasswordEncoder per l'hashing delle password
 * e configura Spring Security per permettere l'accesso a tutte le pagine,
 * poiché l'autenticazione è gestita manualmente tramite HttpSession.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    /**
     * Bean per l'encoding delle password con BCrypt.
     * BCrypt include automaticamente un salt casuale e un fattore di costo
     * adattivo, rendendolo resistente ad attacchi brute-force.
     * 
     * @return un'istanza di BCryptPasswordEncoder
     */
    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura Spring Security per permettere tutte le richieste HTTP.
     * L'autenticazione è gestita manualmente dall'applicazione tramite
     * HttpSession, quindi Spring Security non deve bloccare le pagine.
     * 
     * @param http configurazione HttpSecurity
     * @return la catena di filtri di sicurezza configurata
     * @throws Exception se si verifica un errore nella configurazione
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                .authorizeHttpRequests(auth -> auth
                        .anyRequest().permitAll())
                .csrf(csrf -> csrf.disable())
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable());

        return http.build();
    }
}
