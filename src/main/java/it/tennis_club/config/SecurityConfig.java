package it.tennis_club.config;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import java.util.List;

/**
 * Configurazione di sicurezza — STEP 8: JWT Authentication.
 * 
 * PRIMA (Step 3-6): Tutte le rotte erano pubbliche
 *   .anyRequest().permitAll()
 * 
 * ADESSO (Step 8): Le API sono protette con JWT
 *   /api/auth/**  → pubblica (login/registrazione)
 *   /api/**       → richiede JWT valido
 *   tutto il resto → pubblico (pagine Thymeleaf, risorse statiche)
 * 
 * CONCETTO: SessionCreationPolicy.STATELESS
 * 
 * Con le sessioni server-side (Thymeleaf), il server tiene in memoria
 * "questo utente è loggato". Con JWT, il server NON tiene nulla in memoria.
 * Ogni richiesta porta il token, e il server lo valida al volo.
 * 
 * STATELESS = "non creare sessioni HTTP"
 * Questo è fondamentale per le API REST — ogni richiesta è indipendente.
 */
@Configuration
@EnableWebSecurity
public class SecurityConfig {

    private final JwtAuthFilter jwtAuthFilter;

    @Autowired
    public SecurityConfig(JwtAuthFilter jwtAuthFilter) {
        this.jwtAuthFilter = jwtAuthFilter;
    }

    @Bean
    public BCryptPasswordEncoder passwordEncoder() {
        return new BCryptPasswordEncoder();
    }

    /**
     * Configura la catena di filtri di sicurezza.
     * 
     * ORDINE DELLE REGOLE — conta! Spring le valuta dall'alto verso il basso:
     * 1. /api/auth/** → permitAll (il login DEVE funzionare senza token!)
     * 2. /api/**      → authenticated (tutte le altre API richiedono JWT)
     * 3. /**          → permitAll (Thymeleaf, CSS, JS, pagine statiche)
     * 
     * CONCETTO: addFilterBefore
     * 
     * Registra il nostro JwtAuthFilter PRIMA del filtro di autenticazione
     * standard di Spring. Così il nostro filtro ha la possibilità di
     * impostare l'autenticazione dal token PRIMA che Spring controlli
     * se la richiesta è autorizzata.
     * 
     * Catena dei filtri:
     *   CorsFilter → JwtAuthFilter (nostro) → AuthorizationFilter → Controller
     */
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
        http
                // CORS — permette richieste dal frontend (localhost:3000)
                .cors(cors -> cors.configurationSource(corsConfigurationSource()))

                // CSRF — disabilitato per le API REST
                // CSRF protegge da attacchi su form HTML, ma le API usano token JWT
                .csrf(csrf -> csrf.disable())

                // SESSIONI — stateless (no sessioni server-side)
                .sessionManagement(session -> 
                    session.sessionCreationPolicy(SessionCreationPolicy.STATELESS))

                // REGOLE DI AUTORIZZAZIONE
                //
                // NOTA ARCHITETTURALE: Le GET sono pubbliche perché le pagine
                // /campi e /prenotazioni sono Server Components (girano su Node.js).
                // Il server Node.js NON ha accesso a localStorage del browser,
                // quindi non può inviare il JWT token.
                //
                // Le operazioni di SCRITTURA (POST, DELETE) richiedono il token
                // perché vengono eseguite dal browser (Client Components).
                .authorizeHttpRequests(auth -> auth
                    // Login/registrazione → sempre pubblici
                    .requestMatchers("/api/auth/**").permitAll()
                    // GET su API → pubblici (lettura dati per Server Components)
                    .requestMatchers(org.springframework.http.HttpMethod.GET, "/api/**").permitAll()
                    // POST, PUT, DELETE su API → richiedono JWT
                    .requestMatchers("/api/**").authenticated()
                    // Tutto il resto → pubblico (Thymeleaf, risorse statiche)
                    .anyRequest().permitAll()
                )

                // Disabilita form login e basic auth (usiamo JWT)
                .formLogin(form -> form.disable())
                .httpBasic(basic -> basic.disable())

                // AGGIUNGI IL FILTRO JWT alla catena
                // Il nostro filtro viene eseguito PRIMA di UsernamePasswordAuthenticationFilter
                .addFilterBefore(jwtAuthFilter, UsernamePasswordAuthenticationFilter.class);

        return http.build();
    }

    /**
     * Configurazione CORS — permette richieste dal frontend Next.js.
     */
    @Bean
    public CorsConfigurationSource corsConfigurationSource() {
        CorsConfiguration config = new CorsConfiguration();

        config.setAllowedOrigins(List.of("http://localhost:3000"));
        config.setAllowedMethods(List.of("GET", "POST", "PUT", "DELETE", "OPTIONS"));
        // Importantissimo: "Authorization" deve essere tra gli header permessi
        // altrimenti il browser blocca l'invio del token JWT!
        config.setAllowedHeaders(List.of("Authorization", "Content-Type"));
        config.setAllowCredentials(true);

        UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
        source.registerCorsConfiguration("/api/**", config);

        return source;
    }
}
